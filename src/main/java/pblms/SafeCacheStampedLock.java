package pblms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class SafeCacheStampedLock {

    private final Map<String, String> cache = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    public String get(String key) {
        long stamp = lock.tryOptimisticRead();
        String value = cache.get(key);

        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                value = cache.get(key);
            } finally {
                lock.unlock(stamp);
            }
        }
        return value != null ? value : "N/A";
    }

    public void put(String key, String value) {
        long stamp = lock.writeLock();
        try {
             cache.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
