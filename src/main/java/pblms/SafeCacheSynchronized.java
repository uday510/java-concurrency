package pblms;

import java.util.HashMap;
import java.util.Map;

public class SafeCacheSynchronized {

    private final Map<String, String> cache = new HashMap<>();

    public synchronized String get(String key) {
        return cache.getOrDefault(key, "N/A");
    }

    public synchronized void set(String key, String value) {
        cache.put(key, value);
    }
}
