package no.javazone;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.numRuns;
import static no.javazone.LongRunningTask.task;
import static no.javazone.StatsPrinter.stats;

public class ManualThreadPool {

    public static void main(String[] args) throws Exception {
            BlockingQueue<Integer> ints = new ArrayBlockingQueue<>(numRuns);
            CountDownLatch countDownLatch = new CountDownLatch(numRuns);
            List<Thread> threads = IntStream.range(0, Runtime.getRuntime().availableProcessors() + 5).mapToObj(i -> new Thread(processTasks(ints, countDownLatch))).peek(th -> th.start()).collect(Collectors.toList());
            long t = System.currentTimeMillis();
            IntStream.range(0,numRuns).forEach(i->ints.offer(i));
            countDownLatch.await();
            System.out.println("");
            System.out.println("manual thread pool: " + stats(t));
            threads.forEach(th -> th.interrupt());
    }

    public static Runnable processTasks(BlockingQueue<Integer> ints, CountDownLatch latch) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ints.take();
                    task(latch);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

    }

}
