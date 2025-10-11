package cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceCacheDemo {

    private static final int WRITERS = 1;
    private static final int READERS = 5;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CacheSystem cacheSystem = new CacheSystem();

        // Thread pool of 6 threads (1 writer + 5 readers)
        ExecutorService executor = Executors.newFixedThreadPool(WRITERS + READERS);

        List<Callable<Void>> writerTasks = new ArrayList<>();

        for (int i = 1; i <= WRITERS; i++) {
            int id = i;

            Callable<Void> task = () -> {
             String key = "user:" + id;
             String data = "User " + id + " [updated by " + Thread.currentThread().getName() + "]";
             for (int j = 0; j < 3; j++) {
                 cacheSystem.refresh(key, data + " # " + j);
                 Thread.sleep(500);
             }
              return null;
            };

            writerTasks.add(task);
        }

        Callable<Void> readerTask = () -> {
          for (int i = 0; i < 15; i++) {
              StringBuilder snapshot =  new StringBuilder();
              for (int u = 1; u <= WRITERS; u++) {
                  String key = "user:" + u;
                  snapshot.append(key).
                          append("=").
                          append(cacheSystem.get(key)).
                          append(" | ");
              }
              System.out.println(Thread.currentThread().getName() + " snapshot -> " + snapshot);
              Thread.sleep(300);
          }

          return null;
        };

        for (Callable<Void> task : writerTasks) {
            executor.submit(task);
        }

        for (int i = 1; i <= READERS; i++) {
            executor.submit(readerTask);
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("\nAll writers and reader finished execution.");
    }
}
