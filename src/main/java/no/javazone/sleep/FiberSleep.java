package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import no.javazone.Metrics;

import static no.javazone.RunConfig.numRuns;

public class FiberSleep {

    public static void main(String[] args) throws Exception {
        Metrics metrics = new Metrics(numRuns);
        metrics.runTask(() -> new Fiber(metrics.trackSuspendable(Sleeper::fiberSleep)).start());
    }


}
