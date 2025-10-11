package pblms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SafeCacheReentrantReadWriteLock {

    private final Map<String, String> cache = new HashMap<>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public String get(String key) {
        boolean acquired = false;
        try {
            acquired = readLock.tryLock(10, TimeUnit.MILLISECONDS);

            if (!acquired) {
                System.out.println(Thread.currentThread().getName() + " could NOT acquire read lock in time!");
                return "LOCK_TIMEOUT";
            }

            return cache.getOrDefault(key, "N/A");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return String.valueOf(Thread.currentThread().getState());
        } finally {
            if (acquired) {
                readLock.unlock();
            }
        }
    }

    public void put(String key, String value) throws InterruptedException {
        boolean acquired = false;
        try {
            acquired = writeLock.tryLock(10, TimeUnit.MILLISECONDS);

            if (!acquired) {
                System.out.println(Thread.currentThread().getName() +
                        " could NOT acquire write lock (waited 10ms) — skipping " + key);
                return;
            }

            cache.put(key, value);
            System.out.println(Thread.currentThread().getName() +
                    " wrote " + key + " -> " + value);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired) {
                writeLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SafeCacheReentrantReadWriteLock cache = new SafeCacheReentrantReadWriteLock();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        Callable<Void> writer1 = () -> {

          cache.put("user:1", "Alice");
          try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
          cache.put("user:2", "Bob");

          return null;
        };

        Callable<Void> writer2 = () -> {
            cache.put("user:1", "Dog");
            return null;
        };

        List<Callable<Void>> readers = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            int id = i;
            readers.add(() -> {
               for (int j = 0; j < 5; j++) {
                   String val1 = cache.get("user:1");
                   String val2 = cache.get("user:2");
                   System.out.println("Reader-" + id + " snapshot -> user:1=" + val1 + ", user:2=" + val2);
                   Thread.sleep(200);
               }
               return null;
            });
        }


        executor.submit(writer1);
        executor.submit(writer2);

       executor.invokeAll(readers);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("\n All tasks finished execution.");
    }

}
