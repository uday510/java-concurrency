package locks;

public class User implements Runnable {

    private final Bank bank;
    private final String name;

    public User(String name, Bank bank) {
        this.name = name;
        this.bank = bank;

    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            bank.deposit(10);
            bank.withdraw(5);
            bank.getBalance();
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }

}
