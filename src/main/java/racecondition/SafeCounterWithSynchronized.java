package racecondition;

public class SafeCounterWithSynchronized {

    private long count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized long getCount() {
        return count;
    }

}
