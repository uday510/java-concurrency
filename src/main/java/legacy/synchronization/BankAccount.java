package legacy.synchronization;

public class BankAccount {

    private double balance;
    private String name;

    private final Object lockName = new Object();
    private final Object lockBalance = new Object();

    public BankAccount(double balance, String name) {
        this.balance = balance;
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {

       synchronized (lockName) {
           this.name = name;
           System.out.println("Updated name =  " + name);
       }

    }

    public void deposit(double amount) {

        try {
            System.out.println("Deposit - Talking to the teller at the bank...");
            Thread.sleep(7000);
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }

        synchronized (lockBalance) {
            double originalBalance = balance;
            balance += amount;
            System.out.printf("STARTING BALANCE : %.0f, BALANCE (%.0f)" + " : NEW BALANCE = %.0f%n", originalBalance, amount, balance);
            addPromoDollars(amount); // Reentrant Synchronization
        }
    }

    private void addPromoDollars(double amount) {
        if (amount >= 5000) {
            synchronized (lockBalance) {
                System.out.println("Congratulations! you earned a promotional deposit.");
                balance += 25;
            }
        }
    }

    public synchronized void withdraw(double amount) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }

        double originalBalance = balance;
        if (amount <= balance) {
            balance -= amount;
            System.out.printf("STARTING BALANCE : %.0f, WITHDRAW (%.0f)" + " : NEW BALANCE = %.0f%n", originalBalance, amount, balance);
        } else {
            System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f)" + ": INSUFFICIENT FUNDS!", originalBalance, amount);
        }
    }

}
