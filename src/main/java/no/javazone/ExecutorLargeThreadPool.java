package no.javazone;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.*;
import static no.javazone.StatsPrinter.*;

public class ExecutorLargeThreadPool {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(500);
        CountDownLatch countDownLatch = new CountDownLatch(numRuns);
        long t = System.currentTimeMillis();
        IntStream.range(0,numRuns).forEach(i->executor.submit(() -> task(countDownLatch)));
        countDownLatch.await();
        System.out.println("");
        System.out.println("large thread pool: " + stats(t));
        executor.shutdownNow();
    }

}
