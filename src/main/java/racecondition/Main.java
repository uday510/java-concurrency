package racecondition;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(RaceConditionDemo.THREADS  + " THREADS");
        System.out.println( "1 Million INCREMENTS_PER_THREAD");
        RaceConditionDemo.testCounter(new UnsafeCounter());
        RaceConditionDemo.testCounter(new SafeCounterWithSynchronized());
        RaceConditionDemo.testCounter(new SafeCounterWithLock());
        RaceConditionDemo.testCounter(new SafeCounterWithAtomic());
        RaceConditionDemo.testCounter(new SafeCounterWithLongAdder());
    }
}
