package legacy.threads;

public class RunningThreads {

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
            for (int index = 1; index <= 10; ++index) {
                System.out.print(". ");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    System.out.println("\nWhoops!!" + tname + " interrupted");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("\n" + tname + " completed.");
        });

        Thread installThread = new Thread(() -> {
            try {
                for (int index = 0; index < 3; ++index) {
                    Thread.sleep(250);
                    System.out.println("Installation Step " + (index + 1) + " is completed.");
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }, "InstallThread");

        Thread threadMonitor = new Thread(() -> {
            long now = System.currentTimeMillis();

            while (thread.isAlive()) {
                try {
                    Thread.sleep(1000);
                    if (System.currentTimeMillis() - now > 8000) {
                        thread.interrupt();
                    }
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        System.out.println(thread.getName() + " starting");
        thread.start();
        threadMonitor.start();

        try {
            thread.join();
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }

        if (!thread.isInterrupted()) {
            installThread.start();
        } else {
            System.out.println("Previous thread was interrupted, " + installThread.getName() + " can't run.");
        }

    }

}
