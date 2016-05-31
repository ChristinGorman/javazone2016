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
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TaskRunner taskRunner = new TaskRunner(numRuns);
        Callable<Long> task = taskRunner.track(Sleeper::sleepwalk1Sec);
        taskRunner.runTask(() -> ex.submit(task));
        ex.shutdownNow();
    }
}
