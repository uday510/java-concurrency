package legacy.threads;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        clearConsole();

        var currentThread = Thread.currentThread();
        System.out.println(currentThread.getClass().getName());

        ThreadUtils.printThreadState(currentThread);

        currentThread.setName("MainGuy");
        currentThread.setPriority(Thread.MAX_PRIORITY);
        ThreadUtils.printThreadState(currentThread);

        CustomThread customThread = new CustomThread();
        customThread.start();

        Runnable runnable = () -> {
            for (int index = 1; index <= 8; ++index) {
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

        for (int index = 1; index <= 3; ++index) {
            System.out.print(" 0 ");
            try {
             TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * MIN_PRIORITY = 1
     * NORM_PRIORITY = 5
     * MAX_PRIORITY = 10
     *
     * High priority threads have a better chance of being scheduled, by a
     * thread scheduler, than low priority threads. However, priority behavior can vary
     * across different operating systems and JVM implementations.
     */

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
