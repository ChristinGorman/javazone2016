package no.javazone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;

public class ExecutorLargeThreadPool {

    /**
    Larger thread pool does not help one bit
     */
    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(500);
        TaskRunner printer = new TaskRunner(numRuns);
        printer.runTask(() -> executor.submit(printer.track(Big::task)));
        executor.shutdownNow();
    }

}
