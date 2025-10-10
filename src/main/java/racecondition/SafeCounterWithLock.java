package racecondition;

import java.util.concurrent.locks.ReentrantLock;

public class SafeCounterWithLock {

    private long count = 0;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public void increment() {
        reentrantLock.lock();
        try {
            count++;
        } finally {
            reentrantLock.unlock();
        }
    }

    public long getCount() {
        reentrantLock.lock();
        try {
            return count;
        } finally {
            reentrantLock.unlock();
        }
    }
}
