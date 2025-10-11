package cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorCallableDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            int num = i;
            tasks.add(() -> {
                System.out.println(Thread.currentThread().getName() + " processing " + num);
                Thread.sleep(300);
                return num * num;
            });
        }

        List<Future<Integer>> results = executor.invokeAll(tasks);

        for (Future<Integer> future : results) {
            System.out.println("Result:" + future.get());
        }

        executor.shutdown();
    }
}
