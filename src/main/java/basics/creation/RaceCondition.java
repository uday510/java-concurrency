package basics.creation;

import java.util.Random;

public class RaceCondition {

    int randInt;
    Random random = new Random(System.currentTimeMillis());

    void printer() {

        int i = 1000000;
        while (i != 0) {
            if (randInt % 5 == 0) {
                if (randInt % 5 != 0) {
                    System.out.println(randInt);
                }
            }
            i--;
        }
    }

    void modifier() {

        int i = 1000000;
        while (i != 0) {
            randInt = random.nextInt(1000);
            i--;
        }
    }

    public static void runTest() throws InterruptedException {

        final RaceCondition rc = new RaceCondition();
        Thread t1 = new Thread(rc::printer);

        Thread t2 = new Thread(rc::modifier);


        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        RaceCondition.runTest();
    }

}
