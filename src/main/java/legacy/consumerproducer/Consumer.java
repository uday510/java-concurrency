package legacy.consumerproducer;


import java.util.Random;

public class Consumer implements Runnable {
    private final MessageRepository incomingMessage;

    public Consumer(MessageRepository messageRepository) {
        this.incomingMessage = messageRepository;
    }

    @Override
    public void run() {
        Random random = new Random();
        String latestMessage;
        do {
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
            latestMessage = incomingMessage.read();
            System.out.println(latestMessage);
        } while (!latestMessage.equals("Finished"));
    }

}
