package concurrent_pkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockWithFixedPool {

    private final Map<String, String> cache = new HashMap<>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    private Callable<Void> writeTask(String key, String value) {
        return () -> {
            writeLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " writing: " + key + " -> " + value);
                Thread.sleep(300);
                cache.put(key, value);
                System.out.println(Thread.currentThread().getName() + " done writing: " + key);
            } finally {
                writeLock.unlock();
            }
            return null;
        };
    }

    private Callable<String> readTask(String key) {
        return () -> {
            readLock.lock();
            try {
                Thread.sleep(100);
                String val = cache.getOrDefault(key, "N/A");
                System.out.println(Thread.currentThread().getName() + " reading: " + key + " -> " + val);
                return val;
            } finally {
                readLock.unlock();
            }
        };
    }

    public void runTest() throws InterruptedException, ExecutionException {
        for (int i = 1; i <= 3; i++) {
            executor.submit(writeTask("user:" + i, "Value-" + i));
        }

        List<Future<String>> readers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            readers.add(executor.submit(readTask("user:" + ((i % 3) + 1))));
        }

        for (Future<String> future : readers) {
            future.get();
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("\n Final Cache: " + cache);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ReadWriteLockWithFixedPool().runTest();
    }
}
