package concurrent_pkg.executors_internals;

import java.util.concurrent.*;

public class CustomThreadPoolDemo {

    public static void main(String[] args) {
        ThreadFactory factory = r -> {
          Thread thread = new Thread(r);
          thread.setName("Worker-" + thread.getId());
          return thread;
        };

        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, 4,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2),
                factory,
                handler
        );

        for (int i = 1; i <= 10; i++) {
            int taskId = i;
            pool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " processing task " + taskId);
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            });
        }

        pool.shutdown();
    }
}
