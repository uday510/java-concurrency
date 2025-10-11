package pblms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SafeCacheReentrantLock {

    private final Map<String, String> cache = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public String get(String key) {
        lock.lock();
        try {
            return cache.getOrDefault(key, "N/A");
        } finally {
            lock.unlock();
        }
    }

    public void set(String key, String value) {
        lock.lock();
        try {
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }
}
