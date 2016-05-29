package no.javazone.sleep;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import no.javazone.Metrics;
import no.javazone.RunConfig;

public class VertxNonBlockigSleep {


    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(100));
        EventBus eb = vertx.eventBus();
        Metrics metrics = new Metrics(RunConfig.numRuns);

        eb.consumer("tasks", metrics.trackConsumer(Sleeper::sleepwalk1Sec));
        metrics.runTask(() -> eb.send("tasks",true));
        vertx.close();
    }


}
