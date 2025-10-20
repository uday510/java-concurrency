package basics.creation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownDemo {

    public static void main(String[] args) {


        final ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            pool.submit(() -> {
                System.out.println("Task " + id + " started");
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                System.out.println("Task " + id + " finished");
            });
        }

        pool.shutdown();
        System.out.println("Thread pool stopped accepting new callables.");
    }
}
