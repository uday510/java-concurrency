package locks.synchronized_problems.reentrant_lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReacquireLock {

    static private final ReentrantLock lock = new ReentrantLock();

    static void methodA() {
        lock.lock();
        try {
            methodB();
        } finally {
            lock.unlock();
        }
    }

    static void methodB() {
        lock.lock();
        try {
            System.out.println("Reentered lock successfully.");
        } finally {
            lock.unlock();
        }
    }

    // same thread can lock multiple times - Internally counted
    // when it unlocks the same number of times -> lock fully released
    public static void main(String[] args) {
        methodA();
    }
}
