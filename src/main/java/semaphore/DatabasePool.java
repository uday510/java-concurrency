package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class DatabasePool {
    private static final Semaphore dbSemaphore = new Semaphore(5);


    static void useDatabase(int id) {
        try {
            System.out.println("Client " + id + " waiting for DB connection...");
            dbSemaphore.acquire();
            System.out.println("Client " + id + " connected to DB.");
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        finally {
            System.out.println("Client " + id + " released DB connection.");
            dbSemaphore.release();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 1; i <= 12; i++) {
            final int id = i;
            pool.submit(() -> useDatabase(id));
        }
        pool.shutdown();
    }
}
