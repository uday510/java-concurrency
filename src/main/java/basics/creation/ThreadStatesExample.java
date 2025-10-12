package basics.creation;

public class ThreadStatesExample {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        // Keep reference to main thread
        Thread mainThread = Thread.currentThread();

        // === Watcher Thread ===
        Thread watcher = new Thread(() -> {
            try {
                while (true) {
                    log("Watcher", "Main thread state: " + mainThread.getState());
                    Thread.sleep(500);
                }
            } catch (InterruptedException ignored) {}
        });
        watcher.setDaemon(true);
        watcher.start();

        // === Worker Thread (Thread-1) ===
        Runnable task = () -> {
            log("Thread-1", "Trying to acquire lock...");
            synchronized (lock) {
                log("Thread-1", "Acquired lock! Holding it for 5 seconds...");
                try {
                    Thread.sleep(5000); // holds the lock for 5s
                    log("Thread-1", "Done sleeping. Calling lock.wait() (releases lock)...");
                    lock.wait(); // releases lock, goes to WAITING
                    log("Thread-1", "Woke up from wait, reacquired lock, finishing...");
                } catch (InterruptedException e) {
                    log("Thread-1", "Interrupted!");
                }
            }
            log("Thread-1", "Exiting synchronized block, releasing lock.");
        };

        Thread t = new Thread(task, "Thread-1");
        log("Main", "Thread created: " + t.getName() + " | State: " + t.getState());

        t.start();
        Thread.sleep(100); // give Thread-1 time to start
        log("Main", t.getName() + " current state: " + t.getState());

        // === Main thread trying to acquire lock ===
        log("Main", "Attempting to enter synchronized block...");
        synchronized (lock) { // will block until Thread-1 releases lock after 5 sec
            log("Main", "Acquired lock inside synchronized block!");
            log("Main", "Calling lock.notifyAll() to wake waiting threads...");
            lock.notifyAll();
            log("Main", "Completed notifyAll(), preparing to exit synchronized block.");
        }
        log("Main", "Exited synchronized block (lock released).");

        // === After synchronized block ===
        log("Main", t.getName() + " final state: " + t.getState());
        Thread.sleep(1000);
        log("Main", "Program finished.");
    }

    // Utility logging method with timestamp and thread name
    private static void log(String who, String msg) {
        System.out.printf("[%d ms] [%s] %s%n",
                System.currentTimeMillis() % 100000, who, msg);
    }
}