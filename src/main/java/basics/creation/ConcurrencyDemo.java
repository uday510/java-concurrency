package basics.creation;

public class ConcurrencyDemo {

    public static void main(String[] args) {
        Runnable printTask = () -> {
            String threadName = Thread.currentThread().getName();
            for (int i = 1; i <= 5; i++) {
                System.out.println(threadName + " printing " + i + " at " + System.currentTimeMillis());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {

                }
            }
        };

        Thread t1 = new Thread(printTask, "Worker-1");
        Thread t2 = new Thread(printTask, "Worker-2");

        t1.start();
        t2.start();

    }
}
