package locks;

public class BankSync implements Bank {

    private int balance = 0;

    @Override
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " deposited " + amount);
    }

    @Override
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " withdrawn " + amount);
        }
    }

    @Override
    public synchronized int getBalance() {
        System.out.println(Thread.currentThread().getName() + " read balance " + (this.balance));
        return balance;
    }

    public static void runSimulation(Bank bank, String name) throws InterruptedException {
        System.out.println("\n=== " + name + " ===");
        Thread u1 = new Thread(new User("user-1", bank));
        Thread u2 = new Thread(new User("user-2", bank));
        Thread u3 = new Thread(new User("user-3", bank));

        u1.start(); u2.start(); u3.start();
        u1.join(); u2.join(); u3.join();
        System.out.println("Final balance: " + bank.getBalance());
    }

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new BankSync();
        runSimulation(bank, "Synchronized lock");
    }
}

/**
 * Core Problems with 'synchronized' in Java
 * ----------------------------------------
 * 1. Deadlock possible
 *    - Multiple threads can hold locks in different order and wait forever.
 *
 * 2. No timeout or try-lock support
 *    - Threads block indefinitely; no way to give up or skip lock acquisition.
 *
 * 3. No fairness (no FIFO order)
 *    - Threads acquire locks arbitrarily; may lead to starvation.
 *
 * 4. No interruption support
 *    - Waiting threads cannot be interrupted safely.
 *
 * 5. Single condition queue only
 *    - Only one wait/notify set per monitor; limits complex coordination.
 *
 * 6. No concurrent reads
 *    - Even read-only operations block each other; poor for read-heavy workloads.
 *
 * 7. Lock scope is fixed
 *    - Must acquire and release within the same synchronized block.
 *
 * 8. Synchronizing on wrong objects is easy
 *    - Locking on mutable or new objects breaks mutual exclusion.
 *
 * 9. No monitoring or diagnostics
 *    - No APIs to check lock ownership, queue length, or contention state.
 *
 * 10. Performance degradation under contention
 *     - Heavy blocking, context switching, and poor scalability with many threads.
 */
