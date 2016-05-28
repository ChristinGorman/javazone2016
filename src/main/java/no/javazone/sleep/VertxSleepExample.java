package no.javazone.sleep;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import no.javazone.Big;
import no.javazone.Metrics;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class VertxSleepExample {
    public static void main(String[] args) throws InterruptedException {
        VertxOptions options = new VertxOptions();
        options.setEventLoopPoolSize(100);
        Vertx vertx = Vertx.vertx(options);
        EventBus eb = vertx.eventBus();
        Metrics printer = new Metrics();
        eb.consumer("tasks", idx -> {
            BlockingSleeper.sleep(1000);
            printer.countDown();
        });
        IntStream.range(0, Big.numRuns).forEach(i -> eb.send("tasks",i));
        printer.print();
        vertx.close();
    }
}
