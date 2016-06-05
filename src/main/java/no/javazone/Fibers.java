package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.util.Timer;

import static no.javazone.RunConfig.numRuns;

public class Fibers {

    /**
    Simple syntax, but no performance gain
     */
    public static void main(String[] args) throws Exception{
        Timer.time(() -> {
            TaskRunner taskRunner = new TaskRunner(numRuns);
            SuspendableRunnable task = taskRunner.trackSuspendable(Big::task);
            taskRunner.runTask(() -> new Fiber(task).start());
        });
    }

}
