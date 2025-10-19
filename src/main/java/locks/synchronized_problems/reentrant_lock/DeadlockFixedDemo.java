package locks.synchronized_problems.reentrant_lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockFixedDemo {

    private final Lock lockA = new ReentrantLock();
    private final Lock lockB = new ReentrantLock();

    public void methodA() {
        boolean lockAacquired = false;
        boolean lockBacquired = false;

        try {
            lockAacquired = lockA.tryLock(1, TimeUnit.SECONDS);
            if (lockAacquired) {
                System.out.println(Thread.currentThread().getName() + " acquired lockA");
                Thread.sleep(500);

                lockBacquired = lockB.tryLock(1, TimeUnit.SECONDS);
                if (lockBacquired) {
                    System.out.println(Thread.currentThread().getName() + " acquired lockB");
                } else {
                    System.out.println(Thread.currentThread().getName() + " could not get lockB, skipping work...");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not get lockA, skipping work...");
            }
        } catch (InterruptedException ignored) {}
        finally {
            if (lockBacquired) {
                lockB.unlock();
                System.out.println(Thread.currentThread().getName() + " released lockB");
            }
            if (lockAacquired) {
                lockA.unlock();
                System.out.println(Thread.currentThread().getName() + " released lockA");
            }
        }
    }

    public static void main(String[] args) {
        DeadlockFixedDemo demo = new DeadlockFixedDemo();
        new Thread(demo::methodA, "Thread-1").start();
        new Thread(demo::methodA, "Thread-2").start();
    }
}
