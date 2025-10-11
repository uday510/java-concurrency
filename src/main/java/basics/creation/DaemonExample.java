package basics.creation;

public class DaemonExample {

    public static void main(String[] args) {
        Runnable daemonTask = () -> {
            while (true) {
                System.out.println("[Daemon] Running background cleanup...");
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
        };

        Thread daemon = new Thread(daemonTask);

        daemon.setDaemon(true);
        daemon.start();

        Runnable userTask = new Runnable() {
            @Override
            public void run() {
                System.out.println("[User] Doing main work...");
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        };

        Thread user = new Thread(userTask);

        user.start();
    }
}
