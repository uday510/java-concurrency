package atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderDemo {

    private static final LongAdder pageViews = new LongAdder();
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r);
            t.setName("Worker-" + THREAD_COUNT.getAndIncrement());
            return t;
        };

        ExecutorService pool = Executors.newFixedThreadPool(10, threadFactory);

        Callable<Void> visitor = () -> {
            for (int i = 0; i < 1000; i++) {
                pageViews.increment();
            }
            return null;
        };
        for (int i = 0; i < 10; i++) {
            pool.submit(visitor);
        }

        Thread.sleep(1000);
        pool.shutdown();
        System.out.println("Total page views: " + pageViews.sum());
    }
}
