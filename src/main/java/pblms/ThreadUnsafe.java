package pblms;

import java.util.Random;

public class ThreadUnsafe {

    static Random random = new Random(System.currentTimeMillis());
    static final int incrementPerThread = 100;

    public static void main(String[] args) throws InterruptedException {

        ThreadUnsafeCounter badCounter = new ThreadUnsafeCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < incrementPerThread; i++) {
                badCounter.increment();
                ThreadUnsafe.sleepRandomlyForLessThan10Secs();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < incrementPerThread; i++) {
                badCounter.decrement();
                ThreadUnsafe.sleepRandomlyForLessThan10Secs();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        badCounter.printFinalCounterValue();
   }

    public static void sleepRandomlyForLessThan10Secs() {
        try {
            Thread.sleep(random.nextInt(10));
        } catch (InterruptedException ignored) {

        }
    }
}

class ThreadUnsafeCounter {

    int count = 0;

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    void printFinalCounterValue() {
        System.out.println("counter is: " + count);
    }
}
