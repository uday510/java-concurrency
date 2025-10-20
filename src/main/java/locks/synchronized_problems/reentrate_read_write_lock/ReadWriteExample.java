package locks.synchronized_problems.reentrate_read_write_lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteExample {

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private int data = 0;

    public void write(int value) {
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " writing value " + value);
            data = value;
            Thread.sleep(300);
            System.out.println(Thread.currentThread().getName() + " finished writing.");
        } catch (InterruptedException ignored) {}
        finally {
            rwLock.writeLock().unlock();
        }
    }

    public int read() {
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading value " + data);
            Thread.sleep(200);
            return data;
        } catch (InterruptedException ignored) {}
        finally {
            rwLock.readLock().unlock();
        }
        return data;
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteExample eg = new ReadWriteExample();

        ExecutorService readerExecutorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            readerExecutorService.submit(() -> {
                for (int j = 0; j < 3; j++) {
                    eg.read();
                }
            });
        }

        ExecutorService writerExecutorService = Executors.newSingleThreadExecutor();

        writerExecutorService.submit(() -> {
            for (int i = 1; i <= 3; i++) {
                eg.write(i * 10);
            }
        });

       readerExecutorService.shutdown();
       writerExecutorService.shutdown();

       readerExecutorService.awaitTermination(5, TimeUnit.SECONDS);
       writerExecutorService.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("All read and write operations completed.");

    }
}
