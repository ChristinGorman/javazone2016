package no.javazone;

import co.paralleluniverse.strands.SuspendableRunnable;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import java.text.NumberFormat;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.LongStream;

public class TaskRunner {


    public static class Result {
        public final long duration;
        public final long memory;
        public Result(long duration, long memory) {
            this.duration = duration;
            this.memory = memory;
        }
    }

    private long startTime;
    private CountDownLatch done;
    private final String className;

    public TaskRunner(int numRuns) {
        this.done = new CountDownLatch(numRuns);
        this.startTime = System.currentTimeMillis();
        this.className = new Exception().getStackTrace()[1].getClassName();

    }
    private TaskRunner() {
        this.className = new Exception().getStackTrace()[1].getClassName();
    }

    public void print(Result result) throws InterruptedException {
        done.await(15, TimeUnit.SECONDS);

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        System.out.println(String.format(
                "%s: remaining: %s, duration: %s, memory: %s",
                className.substring(className.lastIndexOf(".")+1),
                format.format(done.getCount()),
                format.format(result.duration),
                format.format(result.memory)));
    }



    public Runnable track(Supplier supplier) {
        return () -> {
            supplier.get();
            done.countDown();
        };
    }
    public SuspendableRunnable trackSuspendable(Supplier supplier) {
        return () -> {
            try {
                supplier.get();
            }finally {
                done.countDown();
            }
        };
    }

    public void countDown() {
        done.countDown();
    }

    public Handler trackConsumer(Supplier task) {
        return (i) -> {
            task.get();
            done.countDown();
        };
    }


    public Result runTask(Runnable task) throws InterruptedException {
        LongStream.range(0, done.getCount()).forEach(i -> task.run());
        Result result = new Result(System.currentTimeMillis() - startTime, Runtime.getRuntime().totalMemory());
        print(result);
        return result;
    }

}
