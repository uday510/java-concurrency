package racecondition;

public class UnsafeCounter {

    private long count = 0;

    public void increment() {
        count++;
    }

    public long getCount() {
        return count;
    }
}
