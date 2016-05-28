package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

import static no.javazone.RunConfig.numRuns;

public class VertxExample {

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(100));
        EventBus eventBus = vertx.eventBus();

        Metrics metrics = new Metrics(numRuns);
        eventBus.consumer("tasks", metrics.trackConsumer(Big::task));
        metrics.runTask(() -> eventBus.send("tasks",true));

        vertx.close();
    }

}
