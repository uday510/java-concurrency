package threads;

public class Main2 {

    public static void main(String[] args) {

        System.out.println("Main thread running");
        try {
            System.out.println("Main thread paused for one second");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
           String tname = Thread.currentThread().getName();
            System.out.println(tname + " should take 10 dots to run.");
            for (int idx = 0; idx < 10; ++idx) {
                System.out.print(". ");
                try {
                    Thread.sleep(500);
//                    System.out.println("A. state = " + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    System.out.println("\nwhoops!! " + tname + " interrupted.");
//                    System.out.println("A1. state = " + Thread.currentThread().getState());
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("\n" + tname + " completed.");
        });

        Thread installThread = new Thread(() -> {
           try {
               for (int idx = 1; idx <= 3; ++idx) {
                   Thread.sleep(250);
                   System.out.println("Installation Step " + (idx) + " is completed.");
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }, "InstallThread");

        Thread threadMonitor = new Thread(() -> {
            long now = System.currentTimeMillis();
            while (thread.isAlive()) {
//                System.out.println("\n waiting for thread to complete");
                try {
                    Thread.sleep(1000);
//                System.out.println("B. state = " + thread.getState());

                    if (System.currentTimeMillis() - now > 8000) {
                        thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(thread.getName() + " starting");
        thread.start();
        threadMonitor.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

//        System.out.println("C. state = " + thread.getState());

        if (!thread.isInterrupted()) {
            installThread.start();
        } else {
            System.out.println("Previous thread was interrupted, " + installThread.getName() + " can't run");
        }
    }
}
