package threads;

public class CustomThread extends Thread {

    @Override
    public void run() {

        for (int index = 1; index <= 5; ++index) {
            System.out.print(" 1 ");
            try {
                Thread.sleep(500); // Sleep for 500 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
