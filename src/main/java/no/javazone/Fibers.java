package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import static no.javazone.RunConfig.numRuns;

public class Fibers {

    public static void main(String[] args) throws Exception{
        TaskRunner taskRunner = new TaskRunner(numRuns);
        SuspendableRunnable task = taskRunner.trackSuspendable(Big::task);
        taskRunner.runTask(() -> new Fiber(task).start());
    }

}
