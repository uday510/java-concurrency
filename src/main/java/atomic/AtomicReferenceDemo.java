package atomic;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    static class Config {
        String version;
        Config(String version) { this.version = version; }
        public String toString() { return "Config v" + version; }
    }

    private static final AtomicReference<Config> config = new AtomicReference<>(new Config("1.0"));

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger cnt = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Worker-" + cnt.getAndIncrement());
                return t;
            }
        };

       final ExecutorService pool = Executors.newCachedThreadPool(threadFactory);

        final Callable<Void> callable = () -> {
            Config oldCfg = config.get();

            double oldV = Double.parseDouble(oldCfg.version);
            Config newCfg = new Config(String.format("%.1f", oldV + 0.1));

            if (config.compareAndSet(oldCfg, newCfg)) {
                System.out.println(Thread.currentThread().getName() + " updated " + oldCfg + " → " + newCfg);
            } else {
                System.out.println(Thread.currentThread().getName() + " failed to update (race detected)");
            }
            return null;
        };

        for (int i = 0; i < 5; i++) {
            pool.submit(callable);
        }

        pool.shutdown();
    }
}
