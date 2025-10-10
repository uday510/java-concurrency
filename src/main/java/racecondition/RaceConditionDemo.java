package racecondition;

public class RaceConditionDemo {

    static final int THREADS = 1000;
    static final int INCREMENTS_PER_THREAD = 100_000;

    public static void testCounter(Object counter) throws InterruptedException {
        Thread[] threads = new Thread[THREADS];

        Runnable task = () -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
                if (counter instanceof UnsafeCounter uc) uc.increment();
                else if (counter instanceof SafeCounterWithSynchronized sc) sc.increment();
                else if (counter instanceof SafeCounterWithLock lc) lc.increment();
                else if (counter instanceof SafeCounterWithAtomic ac) ac.increment();
                else if (counter instanceof SafeCounterWithLongAdder la) la.increment();
            }
        };

        long start = System.currentTimeMillis();

        for (int i = 0; i < THREADS; i++) threads[i] = new Thread(task);
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long end = System.currentTimeMillis();

        long expected = (long) THREADS * INCREMENTS_PER_THREAD;
        long actual = switch (counter) {
            case UnsafeCounter uc -> uc.getCount();
            case SafeCounterWithSynchronized sc -> sc.getCount();
            case SafeCounterWithLock lc -> lc.getCount();
            case SafeCounterWithAtomic ac -> ac.getCount();
            case SafeCounterWithLongAdder la -> la.getCount();
            default -> -1;
        };

        System.out.printf("%-35s | Final: %-10d | Expected: %-10d | Time: %d ms%n",
                counter.getClass().getSimpleName(), actual, expected, (end - start));
    }
}
