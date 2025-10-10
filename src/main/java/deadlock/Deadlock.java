package deadlock;

public class Deadlock {

    private int counter = 0;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    Runnable incrementer = () -> {
        try {
            for (int i = 0; i < 100; i++) {
                incrementCounter();
                System.out.println("Incrementing... " + i);
            }
        } catch (InterruptedException ignored) {}
    };

    Runnable decrementer = () -> {
        try {
            for (int i = 0; i < 100; i++) {
                decrementCounter();
                System.out.println("Decrementing... " + i);
            }
        } catch (InterruptedException ignored) {}
    };

    void incrementCounter() throws InterruptedException {
        synchronized (lock1) {
            System.out.println("Acquired lock1");

            Thread.sleep(100);
            synchronized (lock2) {
                counter++;
            }
        }
    }

    void decrementCounter() throws InterruptedException {
        synchronized (lock2) {
            System.out.println("Acquired lock2");

            Thread.sleep(100);
            synchronized (lock1) {
                counter--;
            }
        }
    }

    public void runTest() throws InterruptedException {

        Thread thread1 = new Thread(incrementer);
        Thread thread2 = new Thread(decrementer);

        thread1.start();
        Thread.sleep(100);
        thread2.start();

        Thread watchdog = new Thread(() -> {
           try {
               Thread.sleep(2000);

               if (thread1.isAlive() && thread2.isAlive()) {
                   System.out.println("Deadlocked, VM will crash in while, goodbye!" );
               }
           } catch (InterruptedException ignored) {}
        });

        watchdog.setDaemon(true);
        watchdog.start();

        thread1.join();
        thread2.join();

        System.out.println("Done : " + counter);
    }

    public static void main(String[] args) {
        Deadlock deadlock = new Deadlock();
        try {
            deadlock.runTest();
        } catch (InterruptedException ignored) {}
    }
}
