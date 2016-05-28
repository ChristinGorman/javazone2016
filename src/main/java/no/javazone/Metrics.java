package no.javazone;

import co.paralleluniverse.common.util.SystemProperties;
import co.paralleluniverse.strands.SuspendableRunnable;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import javax.management.Query;
import java.text.NumberFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Metrics {

    private static ThreadLocal<Runnable> task = new ThreadLocal<>();

    private long startTime;
    private CountDownLatch done;
    private final String className;

    public Metrics(int numRuns) {
        this.done = new CountDownLatch(numRuns);
        this.startTime = System.currentTimeMillis();
        this.className = new Exception().getStackTrace()[1].getClassName();

    }
    private Metrics() {
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
    public SuspendableRunnable trackSuspendable(Supplier<?> supplier) {
        return () -> {
            supplier.get();
            done.countDown();
        };
    }

    public void countDown() {
        done.countDown();
    }

    public <T> Handler<Message<T>> trackConsumer(Supplier<?> task) {
        return (i) -> {
            task.get();
            done.countDown();
        };
    }

    public static Metrics runWithMetrics(Runnable r) {
        task.set(r);
        return new Metrics();
    }

    public void times(int num) throws InterruptedException {
        done = new CountDownLatch(num);
        startTime = System.currentTimeMillis();
        Runnable runnable = task.get();
        runTask(runnable);
    }

    public void runTask(Runnable task) throws InterruptedException {
        LongStream.range(0, done.getCount()).forEach(i -> task.run());
        print();
    }
}
