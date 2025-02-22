package threads;

public class CustomThread extends Thread {

    @Override
    public void run() {

        for (int idx = 1; idx <= 5; ++idx) {
            System.out.print(" 1 ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
