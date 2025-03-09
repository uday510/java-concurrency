package multithreading;

import java.util.concurrent.TimeUnit;

public class CacheData {

    private boolean flag = false;
//    private volatile boolean flag = false;

    public void toggleFlag() {
        flag = !flag;
    }

    public boolean isReady() {
        return flag;
    }

    public static void main(String[] args) {

        CacheData example = new CacheData();

        Thread writerThread = new Thread(() -> {
           try {
               TimeUnit.SECONDS.sleep(1);
           } catch (InterruptedException interruptedException) {
               interruptedException.printStackTrace();
           }
           example.toggleFlag();
           System.out.println("A. Flag set to " + example.isReady());
        });

        Thread readerThread = new Thread(() -> {
           while (!example.isReady()) {
               // Busy waiting
           }
        });

        System.out.println("B. Flag is " + example.isReady());

        writerThread.start();
        readerThread.start();
    }
}
