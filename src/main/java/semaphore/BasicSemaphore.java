package semaphore;

import java.util.concurrent.Semaphore;

public class BasicSemaphore {

    private static final Semaphore semaphore = new Semaphore(3);


    static class Task implements Runnable {
        private final int id;
        Task(int id) { this.id = id; }

        @Override
        public void run() {
            try {
                System.out.println("Task " + id + " waiting for permit...");
                semaphore.acquire();
                System.out.println("Task " + id + " acquired permit");
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            finally {
                System.out.println("Task " + id + " releasing permit.");
                semaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 8; i++) {
            new Thread(new Task(i)).start();
        }
    }
}
