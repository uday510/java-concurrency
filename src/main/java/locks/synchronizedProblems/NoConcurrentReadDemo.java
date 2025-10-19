package locks.synchronizedProblems;

public class NoConcurrentReadDemo {

    private final Object lock = new Object();

    public void readData() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " reading...");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.println(Thread.currentThread().getName() + " done");
        }
    }

    public static void main(String[] args) {
        locks.NoConcurrentReadDemo demo = new locks.NoConcurrentReadDemo();
        for (int i = 1; i <= 3; i++) {
            new Thread(demo::readData, "Reader-" + i).start();
        }
    }

}
