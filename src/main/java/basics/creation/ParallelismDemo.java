package basics.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelismDemo {

    static final long BILLION = 1_000_000_000L;

    private static long heavyComputation(int id) {
        long start = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " started task " + id + " at " + start);

        long sum = 0;
        for (long i = 0; i < BILLION; i++) {
            sum += i;
        }

        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " finished task " + id + " at " + end +
                " (duration: " + (end - start) + " ms)");
        return sum;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);

        ExecutorService executor = Executors.newFixedThreadPool(cores);

        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 1; i <= cores; i++) {
            int id = i;
            futures.add(executor.submit(() -> heavyComputation(id)));
        }

        for (Future<Long> f : futures) {
            f.get();
        }

        executor.shutdown();
        long end = System.currentTimeMillis();

        System.out.println("Total time (ms): " + (end - start));
    }
}
