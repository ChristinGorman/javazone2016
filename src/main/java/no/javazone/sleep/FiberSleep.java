package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.Metrics;

import static no.javazone.RunConfig.numRuns;

public class FiberSleep {

    public static void main(String[] args) throws Exception {
        Metrics metrics = new Metrics(numRuns);
        SuspendableRunnable task = metrics.trackSuspendable(Sleeper::fiberSleep);
        metrics.runTask(() -> new Fiber(task).start());
    }


}
