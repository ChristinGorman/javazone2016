package no.javazone;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.numRuns;
import static no.javazone.LongRunningTask.stats;
import static no.javazone.LongRunningTask.task;

public class Executor {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 5);
        CountDownLatch countDownLatch = new CountDownLatch(numRuns);
        long t = System.currentTimeMillis();
        IntStream.range(0, numRuns).forEach(i -> executor.submit(() -> task(countDownLatch)));
        countDownLatch.await();
        System.out.println("executor: " + stats(t));
        executor.shutdownNow();
    }

}
