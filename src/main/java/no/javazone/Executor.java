package no.javazone;

import no.javazone.util.Timer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    /**
     * Best performance of the lot
     */
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Timer.time(() -> {
            TaskRunner m = new TaskRunner(RunConfig.numRuns);
            m.runTask(() -> executor.submit(m.track(Big::task)));
            executor.shutdownNow();
        });
    }

}
