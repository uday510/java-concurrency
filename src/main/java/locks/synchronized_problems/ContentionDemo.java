package locks.synchronized_problems;

public class ContentionDemo {

    private final Object lock = new Object();
    private int counter = 0;

    public void increment() {
        synchronized (lock) {
            counter++;
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ContentionDemo demo = new ContentionDemo();
        Thread[] threads = new Thread[50];
        for (int i = 0; i < threads.length; i++)
                threads[i] = new Thread(demo::increment, "T-" + i);
        long start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long end = System.currentTimeMillis();
        System.out.println("Counter: " + demo.counter + ", Time: " + (end - start) + "ms");
    }

}
