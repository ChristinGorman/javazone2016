package no.javazone;

import co.paralleluniverse.fibers.Fiber;

import static no.javazone.RunConfig.numRuns;

public class Fibers {

    public static void main(String[] args) throws Exception{
        Metrics metrics = new Metrics(numRuns);
        metrics.runTask(() -> new Fiber(metrics.trackSuspendable(Big::task)).start());
    }

}
