package basics.creation;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";   // Runnable / Active
    private static final String YELLOW = "\u001B[33m";  // Waiting / Timed Waiting
    private static final String RED = "\u001B[31m";     // Blocked
    private static final String BLUE = "\u001B[34m";    // New
    private static final String CYAN = "\u001B[36m";    // Terminated
    private static final String GRAY = "\u001B[90m";    // Daemon output

    public static void main(String[] args) {

        Runnable readTask = () -> {
            readLock.lock();
            try {
                System.out.println(GREEN + Thread.currentThread().getName() + " started reading..." + RESET);
                Thread.sleep(1000);
                System.out.println(GREEN + Thread.currentThread().getName() + " finished reading." + RESET);
            } catch (InterruptedException ignored) {
            } finally {
                readLock.unlock();
            }
        };

        Runnable writeTask = () -> {
            writeLock.lock();
            try {
                System.out.println(RED + Thread.currentThread().getName() + " started writing..." + RESET);
                Thread.sleep(5000);
                System.out.println(RED + Thread.currentThread().getName() + " finished writing." + RESET);
            } catch (InterruptedException ignored) {
            } finally {
                writeLock.unlock();
            }
        };

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(readTask, "Reader-1"));
        threads.add(new Thread(writeTask, "Writer-1"));
        threads.add(new Thread(readTask, "Reader-2"));
        threads.add(new Thread(readTask, "Reader-3"));
        threads.add(new Thread(readTask, "Reader-4"));
        threads.add(new Thread(readTask, "Reader-5"));

        Thread monitor = new Thread(() -> {
            while (true) {
                System.out.println(GRAY + "\n=== Thread States at " + new Date() + " ===" + RESET);
                for (Thread t : threads) {
                    Thread.State state = t.getState();
                    String color = switch (state) {
                        case NEW -> BLUE;
                        case RUNNABLE -> GREEN;
                        case BLOCKED -> RED;
                        case WAITING, TIMED_WAITING -> YELLOW;
                        case TERMINATED -> CYAN;
                    };
                    System.out.println(color + " - " + t.getName() + " : " + state + RESET);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }
        }, "Daemon-Monitor");

        monitor.setDaemon(true);
        monitor.start();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }

        System.out.println("\nAll threads completed. Daemon will stop automatically.");
    }
}