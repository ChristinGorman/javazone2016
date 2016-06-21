package no.javazone.sleep;

import no.javazone.TaskRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutorSleep {

    /**
     *
     * Absolutely hopeless, times out after only completing a tiny fraction
     */
    public static void main(String[] args) throws Exception {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService ex = Executors.newFixedThreadPool(numProcessors);
        new TaskRunner(1_000, Sleeper::sleep1Sec).runOnExecutor(ex);
    }
}
