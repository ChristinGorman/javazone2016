package no.javazone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class Executor {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 5);
        Metrics metrics = new Metrics();
        IntStream.range(0, numRuns).forEach(i ->executor.submit(metrics.track(Big::task)));
        metrics.print();
        executor.shutdownNow();
    }

}
