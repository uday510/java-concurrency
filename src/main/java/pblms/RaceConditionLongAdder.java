package pblms;

import java.util.concurrent.atomic.LongAdder;

public class RaceConditionLongAdder {

    static LongAdder counter = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 1000;
        int incrementsPerThread = 1_000_000;

        Runnable task = () -> {
            for (int i = 0; i < incrementsPerThread; i++) {
                counter.increment();
            }
        };

        Thread[] threads = new Thread[threadCount];

        Runtime runtime = Runtime.getRuntime();

        runtime.gc();

        long startUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long endTime = System.currentTimeMillis();
        long endUsedMem = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Threads = " + threadCount);
        System.out.println("Final count = " + counter.sum());
        System.out.println("Time took = " + (endTime - startTime) + " ms");

        System.out.println("Used memory before = " + startUsedMem / (1024 * 1024) + " MB");
        System.out.println("Used memory after  = " + endUsedMem / (1024 * 1024) + " MB");
        System.out.println("Memory increased by = " + (endUsedMem - startUsedMem) / (1024 * 1024) + " MB");
    }
}