package basics.creation;

public class Demonstration {

    public static void main(String[] args) throws InterruptedException {
        IncorrectSynchronization.runExample();
    }
}

class IncorrectSynchronization {

    Boolean flag = true;

    public void example() throws InterruptedException {

        Runnable r1 = () -> {
          synchronized (flag) {
              try {
                  while (flag) {
                      System.out.println("First thread about to sleep");
                      Thread.sleep(5000);
                      System.out.println("Woke up and about to invoke wait()");
                      flag.wait();
                  }
              } catch (InterruptedException ignored) {}
          }
        };

        Runnable r2 = () -> {
            flag = false;
            System.out.println("Boolean assignment done.");
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        Thread.sleep(1000);
        t2.start();
        t1.join();
        t2.join();
    }

    public static void runExample() throws InterruptedException {
        IncorrectSynchronization incorrectSynchronization = new IncorrectSynchronization();
        incorrectSynchronization.example();
    }
}