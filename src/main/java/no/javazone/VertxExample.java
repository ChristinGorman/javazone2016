package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

import static no.javazone.RunConfig.numRuns;

public class VertxExample {

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(Runtime.getRuntime().availableProcessors()));
        EventBus eventBus = vertx.eventBus();

        TaskRunner taskRunner = new TaskRunner(numRuns);
        eventBus.consumer("tasks", taskRunner.trackConsumer(Big::task));
        taskRunner.runTask(() -> eventBus.send("tasks",true));

        vertx.close();
    }

}
