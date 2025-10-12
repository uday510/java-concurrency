package basics.creation;

public class ThreadInterruption {

    public static void main(String[] args) throws InterruptedException {

        final Thread t1 = new Thread(() -> {

            try {
                System.out.println(System.currentTimeMillis() + " Thread will sleep for 1 hr");
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException ie) {
                System.out.println(ie);
                System.out.println("The interrupt flag is cleared : " + Thread.interrupted() + " " + Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
                System.out.println("Oh someone woke me up ! ");
                System.out.println("The interrupt flag is set now : " + Thread.currentThread().isInterrupted() + " " + Thread.interrupted());
            }
        });

        t1.start();
        System.out.println("About to wake up the sleepy thread ...");
        t1.interrupt();
        System.out.println("Woke up sleepy thread ...");

        t1.join();
    }
}
