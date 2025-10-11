package concurrent_pkg;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrateReadWriteLockDemo {

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    private int value = 0;

    private void write() {
        writeLock.lock();
        try {
             value++;
            System.out.println(Thread.currentThread().getName() + " wrote " + value);
        } finally {
            writeLock.unlock();
        }
    }

    private void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read " + value);
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrateReadWriteLockDemo demo = new ReentrateReadWriteLockDemo();

        Runnable task1 = () -> {
            demo.write();
            demo.read();
        };

        Runnable deamonTask = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        };

        Runnable task2 = demo::read;

        Thread t1 = new Thread(task1, "Thread-0");
        Thread t2 = new Thread(task2, "Thread-1");
        Thread deamon = new Thread(deamonTask);
        deamon.setDaemon(true);

        t1.start();
        deamon.start();
        t2.start();
    }

}
