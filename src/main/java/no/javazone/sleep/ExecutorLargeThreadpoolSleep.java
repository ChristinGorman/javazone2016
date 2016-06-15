package no.javazone.sleep;

import no.javazone.TaskRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorLargeThreadpoolSleep {

    /**
     * Increasing thread pool has dramatic effect
     */
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(500);
        new TaskRunner(1000, Sleeper::sleep1Sec).runOnExecutor(ex);
    }
}
