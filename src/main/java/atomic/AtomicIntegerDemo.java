package atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

    private static final AtomicInteger activeUsers = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();

        Callable<Void> callable = () -> {
            int cur = activeUsers.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + " logged in. Active: " + cur);
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

            cur = activeUsers.decrementAndGet();
            System.out.println(Thread.currentThread().getName() + " logged out. Active: " + cur);
            return null;
        };

        for (int i = 0; i < 5; i++) {
            pool.submit(callable);
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
