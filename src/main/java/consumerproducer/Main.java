package consumerproducer;

import deadlock.Consumer;
import deadlock.MessageRepository;
import deadlock.Producer;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        var messageRepository = new MessageRepository();
        Thread consumer = new Thread(new Consumer(messageRepository));
        Thread producer = new Thread(new Producer(messageRepository));

        consumer.start();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            consumer.join();
            producer.join();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        producer.start();
    }
}
