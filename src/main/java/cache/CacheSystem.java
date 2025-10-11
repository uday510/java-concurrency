package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheSystem {

    private final Map<String, String > cache = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public CacheSystem() {
        Thread monitor = new Thread(this::monitorLockState, "LockMonitor");
        monitor.setDaemon(true);
        monitor.start();
    }

    public String get(String key) {
        readLock.lock();
        try {
            Thread.sleep(200);
            return cache.getOrDefault(key, "NOT_FOUND");
        } catch (InterruptedException ignored) {
            return "INTERRUPTED";
        }
        finally {
            readLock.unlock();
        }
    }

    public void refresh(String key, String value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " updating " + key + " → " + value);
            Thread.sleep(300);
            System.out.println(Thread.currentThread().getName() + " committing update for " + key);
            cache.put(key, value);
        } catch (InterruptedException ignored) {}
        finally {
            writeLock.unlock();
        }
    }

    private void monitorLockState() {
        final String RESET = "\u001B[0m";
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";

        while (true) {
            int readers = rwLock.getReadLockCount();
            boolean isWriteLocked = rwLock.isWriteLocked();
            boolean hasQueued = rwLock.hasQueuedThreads();

            StringBuilder sb = new StringBuilder();
            sb.append(CYAN).append("[LockMonitor] ").append(RESET)
                    .append("Readers: ").append(GREEN).append(readers).append(RESET)
                    .append(", WriteLocked: ").append(isWriteLocked ? RED + "YES" + RESET : "no")
                    .append(", WaitingThreads: ").append(hasQueued ? YELLOW + "YES" + RESET : "no");

            System.out.println(sb);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }

}
