package consumeproducer;

import java.util.Random;

class MessageRepo {

    private String message;
    private boolean hasMessage = false;

    public synchronized String read() {

        while (!hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        hasMessage = false;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {

        while (hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        hasMessage = true;
        notifyAll();
        this.message = message;
    }
}

class MsgWriter implements Runnable {

    private MessageRepo outgoingMessage;
    private final String text = """
            Humpty Dumpty sat on a wall,
            Humpty Dumpty had a great fall,
            All the king's horses and all the king's men,
            Couldn't put Humpty together again.""";

    public MsgWriter(MessageRepo outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] lines = text.split("\n");

        for (String line : lines) {
            outgoingMessage.write(line);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        outgoingMessage.write("Finished");
    }
}

class MsgReader implements Runnable {

    private MessageRepo incomingMessage;

    public MsgReader(MessageRepo incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    @Override
    public void run() {
        Random random = new Random();
        String latestMessage = "";

        do {
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            latestMessage = incomingMessage.read();
            System.out.println(latestMessage);
        } while (!latestMessage.equals("Finished"));
    }
}

public class Deadlock {

    public static void main(String[] args) {

        MessageRepo messageRepo = new MessageRepo();

        Thread reader = new Thread(new MsgReader(messageRepo));
        Thread write = new Thread(new MsgWriter(messageRepo));

        reader.start();
        write.start();
    }
}
