package atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanDemo {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();
        Callable<Void> task = () -> {
            if (started.compareAndSet(false, true)) {
                System.out.println(Thread.currentThread().getName() + " initializing system...");
            } else {
                System.out.println(Thread.currentThread().getName() + " ignored (already initialized");
            }
            return null;
        };

        for (int i = 0; i < 3; i++) {
            pool.submit(task).get();
        }

        pool.shutdown();

    }
}
