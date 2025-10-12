package basics.creation;

public class BadSynchronization2 {

    public static void main(String[] args) {
        Object lock = new Object();

        synchronized (lock) {
            lock.notify();

        }
    }
}
