package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;

import static no.javazone.RunConfig.numRuns;

public class FiberSleep {

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(numRuns);
        SuspendableRunnable task = taskRunner.trackSuspendable(Sleeper::fiberSleep);
        taskRunner.runTask(() -> new Fiber(task).start());
    }


}
