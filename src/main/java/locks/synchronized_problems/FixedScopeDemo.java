package locks.synchronized_problems;

public class FixedScopeDemo {
    private final Object lock = new Object();

    public void step1() {
        synchronized (lock) {
            System.out.println("Step 1 complete");
        }
    }

    public void step2() {
        synchronized (lock) {
            System.out.println("Step 2 complete");
        }
    }

    public static void main(String[] args) {
        new FixedScopeDemo().step1();
    }

}
