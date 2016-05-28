package no.javazone;

import java.text.NumberFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Metrics {

    private final long startTime;
    private final CountDownLatch done;
    private final String className;

    public Metrics() {
        this.done = new CountDownLatch(Big.numRuns);
        this.startTime = System.currentTimeMillis();
        this.className = new Exception().getStackTrace()[1].getClassName();

    }

    public void print() throws InterruptedException {
        done.await(15, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        System.out.println(String.format(
                "%s: remaining: %s, duration: %s, memory: %s",
                className.substring(className.lastIndexOf(".")+1),
                format.format(done.getCount()),
                format.format(duration),
                format.format(Runtime.getRuntime().totalMemory())));
    }

    public <T> Callable<T> track(Supplier<T> supplier) {
        return () -> {
            T returnval = supplier.get();
            done.countDown();
            return returnval;
        };
    }

    public <T> Runnable trackRunnable(Supplier<T> supplier) {
        return () -> {
            supplier.get();
            done.countDown();
        };
    }

    public void countDown() {
        done.countDown();
    }
}
