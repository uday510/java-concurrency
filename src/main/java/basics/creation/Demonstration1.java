package basics.creation;


// Creation using anonymous class inside the Thread class's constructor

public class Demonstration1 {

    public static void main(String[] args) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello, World!");
            }
        });

        t.start();

    }
}
