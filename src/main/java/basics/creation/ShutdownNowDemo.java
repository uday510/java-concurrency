package basics.creation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownNowDemo {

    public static void main(String[] args) throws InterruptedException {

        final ExecutorService pool = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            pool.submit(() -> {
                System.out.println("Task " + id + " started");
                try { Thread.sleep(5000); } catch (InterruptedException e) {
                    System.out.println("Task " + id + " interrupted");
                }
                System.out.println("Task " + id + " exiting...");
            });
        }

        Thread.sleep(1000);
        List<Runnable> pending = pool.shutdownNow();
        System.out.println("Returned tasks: " + pending.size());
    }
}
