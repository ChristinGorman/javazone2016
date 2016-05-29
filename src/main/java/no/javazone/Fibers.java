package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import static no.javazone.RunConfig.numRuns;

public class Fibers {

    public static void main(String[] args) throws Exception{
        Metrics metrics = new Metrics(numRuns);
        SuspendableRunnable task = metrics.trackSuspendable(Big::task);
        metrics.runTask(() -> new Fiber(task).start());
    }

}
