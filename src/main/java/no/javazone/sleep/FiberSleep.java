package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;

import java.util.UUID;

public class FiberSleep {

    /**
    works for any number of fibers - millions
     */
    public static void main(String[] args) throws Exception {
        ThreadLocal<String> value = new ThreadLocal<>();
        TaskRunner taskRunner = new TaskRunner(500_000);
        taskRunner.runTask(() -> {
            new Fiber((SuspendableRunnable)()-> {

                value.set(Thread.currentThread().getName());

                Fiber.sleep(1000);

                System.out.println(value.get() + " "  + Thread.currentThread().getName());
                taskRunner.countDown();
            }).start();
        });
    }


}
