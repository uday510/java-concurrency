package cache;

public class ManualThreadCacheDemo {

    public static void main(String[] args) {
        CacheSystem cacheSystem = new CacheSystem();

        Runnable writeTask = () -> {
            cacheSystem.refresh("user:1", "Alan Turing");
            cacheSystem.refresh("user:2", "Alan Turing1");
        };

        Runnable readTask = () -> {
            for (int j = 0; j < 3; j++) {
                String data = cacheSystem.get("user:1");
                System.out.println(Thread.currentThread().getName() + " read: " + data);
            }
        };

        new Thread(writeTask, "cacheWriter").start();

        for (int i = 1; i <= 5; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }
    }

}
