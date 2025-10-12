package basics.creation;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class ForcedTornRead {
    static final Unsafe unsafe;
    static final long valueOffset;
    static final byte[] bytes = new byte[16];

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            valueOffset = unsafe.arrayBaseOffset(byte[].class) + 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                unsafe.putLong(bytes, valueOffset, 0xFFFFFFFF00000000L);
                unsafe.putLong(bytes, valueOffset, 0x00000000FFFFFFFFL);
            }
        }).start();

        new Thread(() -> {
            while (true) {
                long v = unsafe.getLong(bytes, valueOffset);
                if (v != 0xFFFFFFFF00000000L && v != 0x00000000FFFFFFFFL) {
                    System.out.printf("Torn read: %016X\n", v);
                    System.exit(0);
                }
            }
        }).start();
    }
}