package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import java.text.NumberFormat;
import java.util.Optional;
import java.util.concurrent.*;
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
    private Supplier task;
    private CountDownLatch done;
    private final String className;

    public TaskRunner(int numRuns) {
        this(numRuns, null);
    }

    public TaskRunner(int numRuns, Supplier task) {
        this.task = task;
        this.className = new Exception().getStackTrace()[1].getClassName();
        this.done = new CountDownLatch(numRuns);
        this.startTime = System.currentTimeMillis();
    }

    private void print(Result result) throws Exception{
        print(result, done.getCount(), className);
    }
    public static void print(Result result, long remainingCount, String className) throws InterruptedException {

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        System.out.println(String.format(
                "%s: remaining: %s, duration: %s, memory: %s",
                className.substring(className.lastIndexOf(".")+1),
                format.format(remainingCount),
                format.format(result.duration),
                format.format(result.memory)));
    }


    public void run() throws Exception {
        runOnExecutor(Executors.newSingleThreadExecutor());
    }

    public void runOnExecutor(ExecutorService executor) throws Exception {
        runTask(() -> executor.submit(track(task)));
        executor.shutdownNow();
    }

    public void runOnThread() throws Exception {
        runTask(() -> new Thread(track(task)).start());
    }


    public Runnable track(Supplier supplier) {
        return () -> {
            supplier.get();
            done.countDown();
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


    public Result runTask(Runnable task) throws Exception {
        LongStream.range(0, done.getCount()).forEach(i -> task.run());
        done.await(15, TimeUnit.SECONDS);
        Result result = new Result(System.currentTimeMillis() - startTime, Runtime.getRuntime().totalMemory());
        print(result);
        return result;
    }

}
