package locks.synchronizedProblems;

public class SingleConditionDemo {

    private final Object lock = new Object();
    private boolean conditionA = false;
    private boolean conditionB = false;

    public void waitForA() throws InterruptedException {
        synchronized (lock) {
            while (!conditionA) {
                lock.wait();
            }
            System.out.println("Condition A met!");
        }
    }

    public void waitForB() throws InterruptedException {
        synchronized (lock) {
            while (!conditionB) {
                lock.wait();
            }
            System.out.println("Condition B met!");
        }
    }

    public void signalA() {
        synchronized (lock) {
            conditionA = true;
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        SingleConditionDemo demo = new SingleConditionDemo();
        new Thread(() -> {
            try { demo.waitForA(); } catch (InterruptedException ignored) {}
        }).start();

        new Thread(() -> {
            try { demo.waitForB(); } catch (InterruptedException ignored) {}
        }).start();

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        demo.signalA();
    }

}
