package concurrent_pkg;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccount {

    private double balance = 100.0;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void getBalance(String user) {
        readLock.lock();
        try {
            System.out.println(user + " is checking balance: " + balance);
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}
        finally {
            readLock.unlock();
        }
    }

    public void deposit(String user, double amount) {
        writeLock.lock();
        try {
            System.out.println(user + " is depositing: " + amount);
            double newBalance = balance + amount;
            Thread.sleep(500);
            balance = newBalance;
            System.out.println(user + " new balance: " + balance);
        } catch (InterruptedException ignored) {}
        finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount();

        Runnable readTask = () -> account.getBalance(Thread.currentThread().getName());
        Runnable writeTask = () -> account.deposit(Thread.currentThread().getName(), 200.0);

        for (int i = 1; i <= 3; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }

        new Thread(writeTask, "Writer").start();

        for (int i = 4; i <= 5; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }
    }
}
