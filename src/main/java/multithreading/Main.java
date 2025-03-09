package multithreading;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        StopWatch greenWatch = new StopWatch(TimeUnit.SECONDS);
        StopWatch purpleWatch = new StopWatch(TimeUnit.SECONDS);
        StopWatch redWatch = new StopWatch(TimeUnit.SECONDS);

        Thread green = new Thread(greenWatch::countDown, ThreadColor.ANSI_GREEN.name());
        Thread purple = new Thread(() -> purpleWatch.countDown(7), ThreadColor.ANSI_PURPLE.name());
        Thread red = new Thread(() -> redWatch.countDown(7), ThreadColor.ANSI_RED.name());

        green.start();
        purple.start();
        red.start();
    }

}