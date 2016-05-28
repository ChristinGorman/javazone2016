package no.javazone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 5);
        Metrics m = new Metrics(RunConfig.numRuns);
        m.runTask(() -> executor.submit(m.track(Big::task)));
        executor.shutdownNow();
    }

}
