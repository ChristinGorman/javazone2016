package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

import java.util.stream.IntStream;

public class VertxExample {
    public static void main(String[] args) throws InterruptedException {
        VertxOptions options = new VertxOptions();
        options.setEventLoopPoolSize(100);
        Vertx vertx = Vertx.vertx(options);
        EventBus eb = vertx.eventBus();
        Metrics printer = new Metrics();

        eb.consumer("tasks", idx -> {Big.task(); printer.countDown();});

        IntStream.range(0, Big.numRuns).forEach(i -> eb.send("tasks",i));
        printer.print();
        vertx.close();
    }
}
