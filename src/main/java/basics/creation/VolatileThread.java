package basics.creation;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class VolatileThread {
    // volatile doesn't imply thread-safety!
//    static volatile int count = 0;

    static LongAdder count = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        Callable<Void> callable = () -> {
            for (int i = 0; i < 1000; i++) count.increment();
            return null;
        };

       Callable<Void>[] callables = new Callable[numThreads];

        Arrays.fill(callables, callable);

       for (Callable<Void> c : callables) {
           executorService.submit(c);
       }

       executorService.shutdown();
       executorService.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Expected count: " + (numThreads * 1000));
        System.out.println("Actual count:   " + count);
    }
}
