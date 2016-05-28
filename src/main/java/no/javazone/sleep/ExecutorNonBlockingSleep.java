package no.javazone.sleep;

import no.javazone.Metrics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;

public class ExecutorNonBlockingSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(500);
        Metrics p = new Metrics(numRuns);
        p.runTask(() -> ex.submit(p.track(Sleeper::sleepwalk1Sec)));
    }
}
