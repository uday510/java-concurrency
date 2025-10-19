package locks.synchronizedProblems;

import java.util.Timer;

public class NoTimeoutDemo {
    private final Object lock = new Object();

    public void longTask() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " got lock, working...");
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        }
    }

    public void waitingTask() {
        System.out.println(Thread.currentThread().getName() + " trying to acquire lock...");
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " acquired lock.");
        }
    }

    public static void main(String[] args) {
        NoTimeoutDemo demo = new NoTimeoutDemo();
        new Thread(demo::longTask, "Thread-A").start();
        new Thread(demo::waitingTask, "Thread-B").start();
    }
}
