package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;

public class FiberSleep {

    /**
    works for any number of fibers - millions
     */
    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(500_000);
        taskRunner.runTask(() -> {
            new Fiber((SuspendableRunnable)()-> {
                Fiber.sleep(1000);
                taskRunner.countDown();
            }).start();
        });
    }


}
