package basics.creation;

public class HelloThreads {

    public static void main(String[] args) {

        // Create a new thread (worker)
        Thread worker = new Thread(() -> {
           for (int i = 1; i <= 5; i++) {
               System.out.println("Worker thread: " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });

        // Start the worker thread
        worker.start();

        for (int i = 1; i <= 5; i++) {
            System.out.println("Main thread: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
