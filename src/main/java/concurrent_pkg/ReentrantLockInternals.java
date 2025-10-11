package concurrent_pkg;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInternals {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        System.out.println("Fresh lock:");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        lock.lock();
        System.out.println("After 1st lock:");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        lock.lock();
        System.out.println("After 2nd lock:");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        lock.unlock();
        System.out.println("After 1st unlock");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        lock.unlock();
        System.out.println("After 2nd unlock");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());
    }
}
