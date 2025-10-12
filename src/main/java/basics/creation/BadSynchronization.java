package basics.creation;

public class BadSynchronization {

    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();

        obj.wait();;
    }
}
