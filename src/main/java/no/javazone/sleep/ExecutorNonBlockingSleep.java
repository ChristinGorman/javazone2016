package no.javazone.sleep;

import no.javazone.TaskRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;

public class ExecutorNonBlockingSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(500);
        TaskRunner p = new TaskRunner(numRuns);
        Callable<Long> task = p.track(Sleeper::sleepwalk1Sec);
        p.runTask(() -> ex.submit(task));
        ex.shutdownNow();
    }
}
