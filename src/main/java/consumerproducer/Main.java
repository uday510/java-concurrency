package consumerproducer;

public class Main {

    public static void main(String[] args) {
        var messageRepository = new consumerproducer.MessageRepository();
        Thread consumer = new Thread(new consumerproducer.Consumer(messageRepository));
        Thread producer = new Thread(new consumerproducer.Producer(messageRepository));

        consumer.start();
        producer.start();
    }
}
