package legacy.multithreading;

import java.util.concurrent.TimeUnit;

public class StopWatch {

    private final TimeUnit timeUnit;
    private int index; // Time Slicing

    public StopWatch(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void countDown() {
        countDown(5);
    }

    public void countDown(int unitCount) {

        String threadName = Thread.currentThread().getName();

        ThreadColor threadColor = ThreadColor.ANSI_RED;
        try {
            threadColor = ThreadColor.valueOf(threadName);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Unknown thread name: " + threadName + ", using default color.");
        }

        String color = threadColor.color();

        for (index = unitCount; index > 0; --index) {
            try {
                timeUnit.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(color + "❌ " + threadName + " was interrupted!");
                return;
            }
            System.out.printf("%s%s Thread : index = %d%n", color, threadName, index);
        }

    }
}
