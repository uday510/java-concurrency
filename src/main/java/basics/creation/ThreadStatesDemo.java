package basics.creation;

import java.util.concurrent.locks.*;

public class ThreadStatesDemo {

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String GRAY = "\u001B[90m";

    private static final Object monitor = new Object();
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final Lock writeLock = rwLock.writeLock();

    private static void printState(String color, String label, Thread.State state) {
        System.out.printf("%s%-25s%s%s%n", color, label, state, RESET);
    }

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            try {
                System.out.println(CYAN + Thread.currentThread().getName() + " → RUNNABLE" + RESET);

                // TIMED_WAITING via sleep
                Thread.sleep(500);
                System.out.println(YELLOW + Thread.currentThread().getName() + " → TIMED_WAITING (sleep)" + RESET);

                // WAITING via monitor.wait
                synchronized (monitor) {
                    System.out.println(PURPLE + Thread.currentThread().getName() + " → WAITING (monitor.wait)" + RESET);
                    monitor.wait();
                }
                System.out.println(GREEN + Thread.currentThread().getName() + " → WOKEN from WAITING" + RESET);

                // BLOCKED demonstration
                writeLock.lock();
                try {
                    System.out.println(BLUE + Thread.currentThread().getName() + " → ACQUIRED WRITE LOCK" + RESET);
                } finally {
                    writeLock.unlock();
                }

            } catch (InterruptedException ignored) {}
        };

        Thread t = new Thread(task, "Worker-1");

        printState(GRAY, "Before start:", t.getState());

        t.start();

        new Thread(() -> {
            while (t.getState() != Thread.State.TERMINATED) {
                printState(GRAY, "[Monitor]", t.getState());
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
            printState(GREEN, "[Monitor] Final:", t.getState());
        }, "State-Monitor").start();

        Thread.sleep(100);
        printState(CYAN, "After start:", t.getState());

        Thread.sleep(200);
        printState(YELLOW, "During sleep:", t.getState());

        Thread.sleep(500);
        printState(PURPLE, "During wait:", t.getState());

        synchronized (monitor) {
            monitor.notify(); // Wake the waiting thread
        }

        // Thread to hold write lock
        Thread locker = new Thread(() -> {
            writeLock.lock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            finally {
                writeLock.unlock();
            }
        }, "LockHolder");

        locker.start();
        Thread.sleep(100);
        printState(RED, "Blocked (expected):", t.getState());

        t.join();
        printState(GREEN, "Final:", t.getState());
    }
}