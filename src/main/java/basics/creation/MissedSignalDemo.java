package basics.creation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MissedSignalDemo {

    private static final Object lock = new Object();
    private static boolean dataReady = false;


    public static void main(String[] args) throws InterruptedException {

        Callable<Void> producerTask = () -> {
            synchronized (lock) {
                System.out.println("Producer: producing data...");
                dataReady = true;
                lock.notify();
                System.out.println("Producer: notified");
            }
            return null;
        };

        Callable<Void> consumerTask = () -> {
            synchronized (lock) {
                while (!dataReady) {
                    try {
                        System.out.println("Consumer: busy waiting");
                        lock.wait();
                    } catch (InterruptedException ignored) {}
                }
            }
            return null;
        };

        ExecutorService producer = Executors.newSingleThreadExecutor();
        ExecutorService consumer = Executors.newSingleThreadExecutor();

        producer.submit(producerTask);
        Thread.sleep(500);
        consumer.submit(consumerTask);

        producer.shutdown();
        consumer.shutdown();

        producer.awaitTermination(1, TimeUnit.SECONDS);
        consumer.awaitTermination(1, TimeUnit.SECONDS);
    }
}
