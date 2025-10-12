package basics.creation;


public class WaitNotifyMethods {

    static final Object lock = new Object();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
           synchronized (lock) {
               try {
                   System.out.println(System.currentTimeMillis()  + " T1 waiting...");
                   lock.wait();
                   System.out.println(System.currentTimeMillis() + " T1 resumed");
               } catch (InterruptedException ignored) {}
           }
        });

        Thread t2 = new Thread( () -> {
           synchronized (lock) {
               System.out.println(System.currentTimeMillis() + " T2 is notifying...");
               lock.notify();
               System.out.println(System.currentTimeMillis() + " T2 still holding lock...");
               try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
               System.out.println(System.currentTimeMillis() + " T2 releasing lock...");
           }
        });

        t1.start();
        t2.start();
    }
}
