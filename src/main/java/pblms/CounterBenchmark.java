package pblms;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class CounterBenchmark {

    static final int THREAD_COUNT = 1000;
    static final int INCREMENTS_PER_THREAD = 1_000_000;

    static void benchmarkLongAdder() throws InterruptedException {
        LongAdder counter = new LongAdder();
        Runnable task = () -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) counter.increment();
        };
        runBenchmark("LongAdder", counter::sum, task);
    }

    static void benchmarkAtomicLong() throws InterruptedException {
        AtomicLong counter = new AtomicLong();
        Runnable task = () -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) counter.incrementAndGet();
        };
        runBenchmark("AtomicLong", counter::get, task);
    }

    static void benchmarkSynchronizedLong() throws InterruptedException {
        class SyncCounter {
            private long count = 0;
            synchronized void increment() { count++; }
            synchronized long get() { return count; }
        }
        SyncCounter counter = new SyncCounter();
        Runnable task = () -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) counter.increment();
        };
        runBenchmark("Synchronized long", counter::get, task);
    }

    static void runBenchmark(String name, CounterValueProvider valueProvider, Runnable task) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long startUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        long endTime = System.currentTimeMillis();
        long endUsedMem = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("\n--- " + name + " ---");
        System.out.println("Threads = " + THREAD_COUNT);
        System.out.println("Final count = " + valueProvider.get());
        System.out.println("Time took = " + (endTime - startTime) + " ms");
        System.out.println("Used memory before = " + startUsedMem / (1024 * 1024) + " MB");
        System.out.println("Used memory after  = " + endUsedMem / (1024 * 1024) + " MB");
        System.out.println("Memory increased by = " + (endUsedMem - startUsedMem) / (1024 * 1024) + " MB");
    }

    interface CounterValueProvider {
        long get();
    }

    public static void main(String[] args) throws InterruptedException {
        benchmarkLongAdder();
        benchmarkAtomicLong();
        benchmarkSynchronizedLong();
    }

    /**
     *
     *
     * --- LongAdder ---
     * Threads = 1000
     * Final count = 1000000000
     * Time took = 1722 ms
     * Used memory before = 1 MB
     * Used memory after  = 2 MB
     * Memory increased by = 1 MB
     *
     * --- AtomicLong ---
     * Threads = 1000
     * Final count = 1000000000
     * Time took = 56147 ms
     * Used memory before = 1 MB
     * Used memory after  = 1 MB
     * Memory increased by = 0 MB
     *
     * --- Synchronized long ---
     * Threads = 1000
     * Final count = 1000000000
     * Time took = 41746 ms
     * Used memory before = 1 MB
     * Used memory after  = 1 MB
     * Memory increased by = 0 MB
     */
}