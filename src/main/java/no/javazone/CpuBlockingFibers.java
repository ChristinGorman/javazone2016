package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

public class CpuBlockingFibers {

    /**
     * Simple syntax, but no performance gain
     */
    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(1000);

        taskRunner.runTask(() -> new Fiber((SuspendableRunnable) () -> {
            Big.task();
            taskRunner.countDown();
        }).start());
    }

}
