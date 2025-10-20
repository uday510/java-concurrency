package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThreadPoolSemaphore {
    private static final Semaphore semaphore = new Semaphore(3);

    static void runTask(int id) {
        try {
            System.out.println("Task " + id + " waiting...");
            semaphore.acquire();
            System.out.println("Task " + id + " running...");
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}
        finally {
            System.out.println("Task " + id + " done.");
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            int id = i;
            pool.submit(() -> runTask(id));
        }

        pool.shutdown();
    }
}
