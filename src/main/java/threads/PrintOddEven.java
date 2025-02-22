package threads;

class OddThread extends Thread {
    @Override
    public void run() {
        for (int idx = 1; idx <= 10; idx += 2) {
            System.out.println("OddThread: " + idx);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("OddThread interrupted!");
                break;
            }
        }
    }
}

class EvenRunnable implements Runnable {
    public void run() {
        for (int idx = 2; idx <= 10; idx += 2) {
            System.out.println("EvenThread: " + idx);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("EvenThread interrupted!");
                break;
            }
        }
    }
}

public class PrintOddEven {

    public static void main(String[] args) {

        OddThread oddThread = new OddThread();
        Runnable runnable = () -> {
            for (int idx = 2; idx <= 10; idx += 2) {
                System.out.println("EvenThread: " + idx);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("EvenThread interrupted!");
                    break;
                }
            }
        };
//        Thread evenThread = new Thread(new EvenRunnable());
        Thread evenThread = new Thread(runnable);

        oddThread.start();
        evenThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        oddThread.interrupt();
//        evenThread.interrupt();
    }
}
