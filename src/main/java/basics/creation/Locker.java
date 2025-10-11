package basics.creation;

public class Locker {

    synchronized void criticalSection(String name) {
        System.out.println(name + " entered");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        System.out.println(name + " exiting");
    }

    public static void main(String[] args) {
        Locker locker = new Locker();
        Thread t1 = new Thread(() -> locker.criticalSection("Thread-1"));
        Thread t2 = new Thread(() -> locker.criticalSection("Thread-2"));

        t1.start();
        t2.start();
    }
}
