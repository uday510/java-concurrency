package legacy.shoewarehouse;

import java.util.ArrayList;
import java.util.List;

public class ShoeWareHouse {

    private List<Order> shippingItems;
    public final static String[] PRODUCT_LIST =
            {"Running Shoes", "Sandals", "Boots", "Slippers", "High Tops"};


    public ShoeWareHouse() {
        this.shippingItems = new ArrayList<>();
    }

    public synchronized void receiveOrder(Order item) {
        while (shippingItems.size() > 20) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        shippingItems.add(item);
        System.out.println("Incoming: " + item.toString());
        notifyAll();
    }


    public synchronized Order fulfillOrder() {

        while (shippingItems.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Order fulfilledOrder = shippingItems.removeFirst();
        System.out.println(Thread.currentThread().getName() + " Fulfilled: " + fulfilledOrder.toString());
        notifyAll();
        return fulfilledOrder;
    }


}
