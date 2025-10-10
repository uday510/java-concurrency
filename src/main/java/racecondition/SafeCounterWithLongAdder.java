package racecondition;

import java.util.concurrent.atomic.LongAdder;

public class SafeCounterWithLongAdder {

    private final LongAdder count = new LongAdder();

    public void increment() {
        count.increment();
    }

    public long getCount() {
        return count.sum();
    }

}
