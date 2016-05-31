package no.javazone.sleep;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import no.javazone.TaskRunner;
import no.javazone.RunConfig;

public class VertxSleepExample {

    /**
    nope, no chance
     */
    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(100));
        EventBus eb = vertx.eventBus();
        TaskRunner taskRunner = new TaskRunner(RunConfig.numRuns);


        eb.consumer("tasks", (p) -> vertx.executeBlocking(
                future -> {Sleeper.sleep1Sec(); future.complete(true);},
                res -> taskRunner.countDown()));

        taskRunner.runTask(() -> eb.send("tasks", true));
        vertx.close();
    }

}
