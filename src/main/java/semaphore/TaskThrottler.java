package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TaskThrottler {
    private static final Semaphore throttle = new Semaphore(4);

    static void heavyTask(int id) {
        try {
            throttle.acquire();
            System.out.println("Heavy task " + id + " started.");
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
        finally {
            System.out.println("Heavy task " + id + " finished");
            throttle.release();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 1; i <= 12; i++) {
            int id = i;
            pool.submit(() -> heavyTask(id));
        }

        pool.shutdown();
    }
}
