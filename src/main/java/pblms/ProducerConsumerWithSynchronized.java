package pblms;

import java.util.concurrent.*;

public class ProducerConsumerWithSynchronized {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        ThreadFactory threadFactory = new ThreadFactory() {
            private int cnt = 1;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Worker-" + cnt++);
                return t;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(3, threadFactory);

        Callable<Void> writer = () -> {

            try {
                for (int i = 0; i < 50; i++) {
                    queue.enqueue(i);
                    System.out.println(Thread.currentThread().getName() + " enqueued " + i);
                }
            } catch (InterruptedException ignored) {}

            return null;
        };

        Callable<Void> reader = () -> {

            try {
                for (int i = 0; i < 25; i++) {
                    System.out.println(Thread.currentThread().getName() + " dequeued " + queue.deque());
                }
            } catch (InterruptedException ignored) {}

            return null;
        };

        Future<Void> writerFuture = pool.submit(writer);
        Future<Void> readerFuture1 = pool.submit(reader);
        Future<Void> readerFuture2 = pool.submit(reader);

        writerFuture.get();
        readerFuture1.get();
        readerFuture2.get();

        System.out.println("All threads done!");
        pool.shutdown();

    }
}


class BlockingQueue<T> {

    T[] array;
    final Object lock = new Object();
    int size = 0;
    int capacity;
    int head = 0;
    int tail = 0;

    @SuppressWarnings("unchecked")
    public BlockingQueue(int capacity) {
        array = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {

        synchronized (lock) {

            while (size == capacity) {
                lock.wait();
            }

            tail %= capacity;
            array[tail] = item;
            size++;
            tail++;
            lock.notifyAll();
        }
    }

    public T deque() throws InterruptedException {

        T item;
        synchronized (lock) {

            while (size == 0) {
                lock.wait();
            }

            head %= capacity;
            item = array[head];
            array[head] = null;
            head++;
            size--;
            lock.notifyAll();
        }

        return item;
    }
}