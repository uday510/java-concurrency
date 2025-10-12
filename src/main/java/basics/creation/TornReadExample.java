package basics.creation;

public class TornReadExample {

    static class SharedData {
        static long value = 0L;
    }

    public static void main(String[] args) {

        Runnable writeTask = () -> {
            while (true) {
                SharedData.value = 0xFFFFFFFF00000000L;
                Thread.yield();
                SharedData.value = 0x00000000FFFFFFFFL;
                Thread.yield();
            }
        };

        Runnable readTask = () -> {
          while (true) {
              long v = SharedData.value;

              if (v != 0xFFFFFFFF00000000L && v != 0x00000000FFFFFFFFL) {
                  System.out.printf("Torn read detected: %016X\n", v);
                  System.exit(-1);
              } else {
                  System.out.println("Not detected.");
              }
          }
        };


        Thread readThread = new Thread(readTask);
        Thread writeThread = new Thread(writeTask);

        writeThread.start();
        readThread.start();


    }


}
