package consumerproducer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    private final Lock lock = new ReentrantLock();

    public String read() {

        while (!hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        hasMessage = false;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {

        while (hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        hasMessage = true;
        notifyAll();
        this.message = message;
    }
}
