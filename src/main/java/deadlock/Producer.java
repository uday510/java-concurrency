package deadlock;

import java.util.Random;

public class Producer implements Runnable {
    private final MessageRepository outgoingMessages;
    private final String text = """
            Humpty Dumpty sat on a wall,
            Humpty Dumpty had a great fall,
            All the king's horses and all the king's men,
            Couldn't put Humpty together again.""";

    public Producer(MessageRepository outgoingMessages) {
        this.outgoingMessages = outgoingMessages;
    }

    @Override
    public void run() {

        Random random = new Random();
        String[] lines = this.text.split("\n");

        for (int index = 0; index < lines.length; ++index) {
            outgoingMessages.write(lines[index]);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        outgoingMessages.write("Finished");
    }

}
