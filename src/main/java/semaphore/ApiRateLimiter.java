package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ApiRateLimiter {

    private static final Semaphore limiter = new Semaphore(3);

    static void makeApiCall(int client) {
        try {
            if (limiter.tryAcquire(1, TimeUnit.SECONDS)) {
                System.out.println("Client " + client + " calling API...");
                Thread.sleep(100);
            } else {
                System.out.println("Client " + client + " rejected (too many calls).");
            }
        } catch (InterruptedException ignored) {}
        finally {
            limiter.release();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 1; i <= 10; i++) {
            final int client = i;
            pool.submit(() -> makeApiCall(client));
        }
        pool.shutdown();
    }

}
