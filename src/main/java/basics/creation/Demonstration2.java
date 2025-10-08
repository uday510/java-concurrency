package basics.creation;

// Thread creation using with class
public class Demonstration2 {

    public static void main(String[] args) {

        ExecuteMe executeMe = new ExecuteMe();
        Thread t = new Thread(executeMe);
        t.start();

    }
}

class ExecuteMe implements Runnable {

    public void run() {
        System.out.println("New Thread");
    }

}
