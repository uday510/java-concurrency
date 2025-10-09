package basics.creation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelismDemo {

    static final long BILLION = 1_000_000_000L;

    private static long heavyComputation(int id) {
        System.out.println(Thread.currentThread().getName() + " finished at " + System.currentTimeMillis());
        long sum = 0;
        for (long i = 0; i < BILLION; i++) {
            sum += i;
        }
        System.out.println(Thread.currentThread().getName() + " finished at " + System.currentTimeMillis());
        return sum;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException  {
        long start = System.currentTimeMillis();

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);

        ExecutorService executor = Executors.newFixedThreadPool(cores);

        Future<Long> f1 = executor.submit(() -> heavyComputation(1));
        Future<Long> f2 = executor.submit(() -> heavyComputation(2));

        f1.get();
        f2.get();

        executor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("Total time (ms): " + (end - start));
    }
}
