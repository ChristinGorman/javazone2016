package no.javazone.sleep;

import no.javazone.Metrics;
import no.javazone.RunConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(500);
        Metrics metrics = new Metrics(RunConfig.numRuns);
        metrics.runTask(() -> ex.submit(metrics.track(Sleeper::sleep1Sec)));
    }
}
