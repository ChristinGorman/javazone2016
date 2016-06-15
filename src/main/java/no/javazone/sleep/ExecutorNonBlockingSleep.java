package no.javazone.sleep;

import no.javazone.TaskRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;

public class ExecutorNonBlockingSleep {

    /**
     *
     * Still absolutely hopeless times out after only completing a tiny fraction
     */
    public static void main(String[] args) throws Exception {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService ex = Executors.newFixedThreadPool(numProcessors);
        new TaskRunner(1000, Sleeper::sleepwalk1Sec).runOnExecutor(ex);
    }
}
