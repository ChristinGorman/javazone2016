package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import no.javazone.util.Timer;

import static no.javazone.RunConfig.numRuns;

public class VertxExample {

    /**
     * Syntax sucks, and the performance isn't great either.
     */
    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(Runtime.getRuntime().availableProcessors()));
        EventBus eventBus = vertx.eventBus();

        Timer.time(() -> {
            TaskRunner taskRunner = new TaskRunner(numRuns);
            eventBus.consumer("tasks", taskRunner.trackConsumer(Big::task));
            taskRunner.runTask(() -> eventBus.send("tasks", true));
        });

        vertx.close();
    }

}
