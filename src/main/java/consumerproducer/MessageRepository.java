package consumerproducer;

public class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    public synchronized String read() {

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
