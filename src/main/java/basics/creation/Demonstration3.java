package basics.creation;


// Thread Creation by extending Thread class
public class Demonstration3 {

    public static void main(String[] args) throws InterruptedException {
        Executee executee = new Executee();
        executee.start();
        executee.join();

    }
}

class Executee extends Thread {

    @Override
    public void run() {
        System.out.println("Thread Creation by extending Thread class");
    }

}
