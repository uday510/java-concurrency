package legacy.consumerproducer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    private final Lock lock = new ReentrantLock();

    public String read() {
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                try {
                    while (!hasMessage) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    hasMessage = false;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("** read blocked " + lock);
                hasMessage = false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public void write(String message) {
        if (lock.tryLock()) {
            try {
                while (hasMessage) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                hasMessage = true;
                this.message = message;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("** write blocked " + lock);
            hasMessage = true;
        }
        this.message = message;
    }
}
