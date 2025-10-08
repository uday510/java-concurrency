package legacy.shoewarehouse;

import java.util.Random;

record Order(long orderId, String item, int quantity) {}

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {

        ShoeWareHouse wareHouse = new ShoeWareHouse();
        Thread producerThread = new Thread(() -> {
            for (int index = 0; index < 10; index++) {
                wareHouse.receiveOrder(new Order(
                        random.nextLong(1000000, 9999999),
                        ShoeWareHouse.PRODUCT_LIST[random.nextInt(0, 5)],
                        random.nextInt(1, 4)
                ));
            }
        });

        producerThread.start();

        for (int index = 0; index < 2; index++) {
            Thread consumerThread = new Thread(() -> {
               for  (int index2 = 0; index2 < 5; index2++) {
                   Order item = wareHouse.fulfillOrder();
               }
            });
            consumerThread.start();
        }

    }

}
