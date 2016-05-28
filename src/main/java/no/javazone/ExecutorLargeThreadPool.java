package no.javazone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class ExecutorLargeThreadPool {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(500);
        Metrics printer = new Metrics();
        IntStream.range(0,numRuns).forEach(i->executor.submit(() -> printer.track(Big::task)));
        printer.print();
        executor.shutdownNow();
    }

}
