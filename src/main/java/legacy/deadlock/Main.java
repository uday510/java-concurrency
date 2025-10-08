package legacy.deadlock;

public class Main {

    public static void main(String[] args){

        var messageRepository = new MessageRepository();
        Thread consumer = new Thread(new Consumer(messageRepository));
        Thread producer = new Thread(new Producer(messageRepository));

        consumer.start();
        producer.start();
    }

}
