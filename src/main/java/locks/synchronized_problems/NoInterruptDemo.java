package locks.synchronized_problems;

public class NoInterruptDemo {
    private final Object lock = new Object();

    private void longHold() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " holding lock.");
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        }
    }

    public void waitingThread() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NoInterruptDemo demo = new NoInterruptDemo();
        Thread t1 = new Thread(demo::longHold, "Thread-1");
        Thread t2 = new Thread(demo::waitingThread, "Thread-2");

        t1.start();
        Thread.sleep(200);
        t2.start();

        Thread.sleep(1000);
        System.out.println("Interrupting Thread-2...");
        t2.interrupt();
    }
}
