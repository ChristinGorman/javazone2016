package no.javazone.sleep;

import no.javazone.Metrics;
import no.javazone.RunConfig;

public class ThreadSleep {

    public static void main(String[] args) throws Exception {
        Metrics metrics = new Metrics(RunConfig.numRuns);
        Runnable task = metrics.trackRunnable(Sleeper::sleep1Sec);
        metrics.runTask(() -> new Thread(task).start());
    }
}
