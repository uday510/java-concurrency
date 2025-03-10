package consumerproducer;

public class Main {

    public static void main(String[] args) {
        var messageRepository = new consumerproducer.MessageRepository();
        Thread consumer = new Thread(new consumerproducer.Consumer(messageRepository), "consumer");
        Thread producer = new Thread(new consumerproducer.Producer(messageRepository), "producer");

        producer.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.print("Producer had exception: " + exception);
            if (consumer.isAlive()) {
                System.out.println("Going to interrupt the consumer");
                consumer.interrupt();
            }
        });

        consumer.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.print("Consumer had exception: " + exception);
            if (producer.isAlive()) {
                System.out.println("Going to interrupt the producer");
                producer.interrupt();
            }
        });

        consumer.start();
        producer.start();
    }
}
