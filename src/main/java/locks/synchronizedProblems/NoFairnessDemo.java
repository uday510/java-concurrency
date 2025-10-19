package locks.synchronizedProblems;

public class NoFairnessDemo {

    private final Object lock = new Object();

    public void access() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + "acquired lock.");
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) {
        NoFairnessDemo demo = new NoFairnessDemo();
        for (int i = 1; i <= 5; i++) {
            new Thread(demo::access, "Thead-" + i).start();
        }
    }
}
