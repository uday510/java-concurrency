package locks.synchronizedProblems;

public class DeadlockDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    public void methodA() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + " acquired lockA, waiting for lockB...");
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (lockB) {
                System.out.println("Acquired both locks!");
            }
        }
    }

    public void methodB() {
        synchronized (lockB) {
            System.out.println(Thread.currentThread().getName() + " acquired lockB, waiting for lockA...");
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (lockA) {
                System.out.println("Acquired both lock!");
            }
        }
    }

    public static void main(String[] args) {
        DeadlockDemo demo = new DeadlockDemo();
        new Thread(demo::methodA, "Thread-1").start();
        new Thread(demo::methodB, "Thread-2").start();
    }
}
