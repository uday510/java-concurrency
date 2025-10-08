package pblms;

import java.util.concurrent.atomic.LongAdder;

public class RaceConditionLongAdder {

    static LongAdder counter = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);

        long start = System.currentTimeMillis();

        t1.start(); t2.start(); t3.start(); t4.start();
        t1.join(); t2.join(); t3.join(); t4.join();

        long end = System.currentTimeMillis();

        System.out.println("Final count = " + counter.sum());
        System.out.println("Time took = " + (end - start) + " ms");
    }
}
