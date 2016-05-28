package no.javazone;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class ManualThreadPool {

    public static void main(String[] args) throws Exception {
        BlockingQueue<Integer> ints = new ArrayBlockingQueue<>(numRuns);
        Metrics printer = new Metrics();
        List<Thread> threads = IntStream.range(0, Runtime.getRuntime().availableProcessors() + 5)
                .mapToObj(i -> new Thread(processTasks(ints, printer)))
                .peek(th -> th.start())
                .collect(Collectors.toList());
        IntStream.range(0, numRuns).forEach(i -> ints.offer(i));
        printer.print();
        threads.forEach(th -> th.interrupt());
    }

    public static Runnable processTasks(BlockingQueue<Integer> ints, Metrics latch) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ints.take();
                    Big.task();
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

    }

}
