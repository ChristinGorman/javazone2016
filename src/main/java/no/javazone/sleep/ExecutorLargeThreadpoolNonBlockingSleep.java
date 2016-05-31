package no.javazone.sleep;

import no.javazone.RunConfig;
import no.javazone.TaskRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorLargeThreadpoolNonBlockingSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(500);
        TaskRunner taskRunner = new TaskRunner(RunConfig.numRuns);
        Callable<Long> task = taskRunner.track(Sleeper::sleepwalk1Sec);
        taskRunner.runTask(() -> ex.submit(task));
        ex.shutdownNow();
    }
}
