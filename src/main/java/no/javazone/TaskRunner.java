package no.javazone;

import io.vertx.core.Handler;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TaskRunner {
    private static final long TIMEOUT_MILLIS = 15_000;

    public static class Result {

        public final long duration;

        public final long memory;

        public Result(long duration, long memory) {
            this.duration = duration;
            this.memory = memory;
        }

    }
    private long startTime;
    private int numRuns;
    private Supplier task;
    private CountDownLatch done;
    private final String className;

    public TaskRunner(int numRuns) {
        this(numRuns, null);
    }

    public TaskRunner(int numRuns, Supplier task) {
        this.numRuns = numRuns;
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


    Thread memoryPrintThread = new Thread(()->{
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int mem = (int) ((Runtime.getRuntime().totalMemory() * 100) / Runtime.getRuntime().maxMemory());
                StringBuilder builder = new StringBuilder();
                IntStream.range(0,mem/2).forEach(i->builder.append('\u2588'));
                builder.append(" " + mem + "%");
                IntStream.range(Math.max(0,50 - (50 - builder.length())),50).forEach(i->builder.append(" "));
                long progress = ((numRuns - done.getCount()) * 100) / numRuns;
                long timeoutProgress = Math.min(100, ((System.currentTimeMillis() - startTime )* 100) / TIMEOUT_MILLIS);
                String line = "\rMemory usage: " + builder.toString() + " (progress: " + Math.max(progress, timeoutProgress) + "%)";
                System.out.print(line);
                System.out.flush();
                Thread.sleep(100);
            }catch(InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public Result runTask(Runnable task) throws Exception {
        memoryPrintThread.start();
        LongStream.range(0, done.getCount()).forEach(i -> task.run());
        done.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        memoryPrintThread.interrupt();
        System.out.println();
        Result result = new Result(System.currentTimeMillis() - startTime, Runtime.getRuntime().totalMemory());
        print(result);
        return result;
    }
}
