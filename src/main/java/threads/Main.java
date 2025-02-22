package threads;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        var currentThread = Thread.currentThread();
        System.out.println(currentThread.getClass().getName());

        System.out.println(currentThread);
        printThreadState(currentThread);

        currentThread.setName("Main Thread");
        currentThread.setPriority(Thread.MAX_PRIORITY);
        printThreadState(currentThread);

        CustomThread customThread = new CustomThread();
//        customThread.run(); // run method runs synchronously
        customThread.start(); // start method runs asynchronously

        Runnable runnable = () -> {
            for (int idx = 1; idx <= 8; ++idx) {
                System.out.print(" 2 ");
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        for (int idx = 1; idx <= 3; ++idx) {
            System.out.print(" 0 ");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printThreadState(Thread thread) {

        System.out.println("--------------------------");
        System.out.println("Thread ID: " + thread.threadId());
        System.out.println("Thread Name: " + thread.getName());
        System.out.println("Thread State: " + thread.getState());
        System.out.println("Thread Priority: " + thread.getPriority());
        System.out.println("Thread Group: " + thread.getThreadGroup());
        System.out.println("Thread is Alive: " + thread.isAlive());
        System.out.println("Thread is Daemon: " + thread.isDaemon());
        System.out.println("Thread is Interrupted: " + thread.isInterrupted());
        System.out.println("----------------------------");

    }
}
