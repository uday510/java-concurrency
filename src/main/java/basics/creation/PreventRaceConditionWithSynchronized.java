package basics.creation;

import java.util.Random;

public class PreventRaceConditionWithSynchronized {

    int randInt;
    Random random = new Random(System.currentTimeMillis());

    void printer() {

        int i = 1000000;
        while (i != 0) {
            synchronized (this) {
                if (randInt % 5 == 0) {
                    if (randInt % 5 != 0) {
                        System.out.println(randInt);
                    }
                }
            }
            i--;
        }
    }

    void modifier() {

        int i = 1000000;
        while (i != 0) {
            synchronized (this) {
                randInt = random.nextInt(1000);
                i--;
            }
        }
    }

    public static void runTest() throws InterruptedException {


        final PreventRaceConditionWithSynchronized rc = new PreventRaceConditionWithSynchronized();

        Thread t1 = new Thread(rc::printer);
        Thread t2 = new Thread(rc::modifier);


        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

    public static void main(String[] args) throws InterruptedException {
        PreventRaceConditionWithSynchronized.runTest();
    }

}
