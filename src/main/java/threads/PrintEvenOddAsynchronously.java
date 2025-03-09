package threads;

class OddThread extends Thread {

    @Override
    public void run() {
        for (int index = 1; index <= 10; index += 2) {
            System.out.println("OddThread: " + index);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("OddThread interrupted!");
                break;
            }
        }
    }

}

class EvenRunnable implements Runnable {

    @Override
    public void run() {
        for (int index = 2; index <= 10; index += 2) {
            System.out.println("EvenThread: " + index);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("EvenThread interrupted!");
                break;
            }
        }
    }

}

public class PrintEvenOddAsynchronously {

    public static void main(String[] args) {
        OddThread oddThread = new OddThread();
        Runnable runnable = (() -> {
            for (int index = 2; index <= 10; index += 2) {
                System.out.println("EvenThread: " + index);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    System.out.println("EvenThread interrupted!");
                    break;
                }
            }
        });

        Thread evenThread = new Thread(runnable);
        oddThread.start();
        evenThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        oddThread.interrupt();
    }

}
