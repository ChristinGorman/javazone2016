package no.javazone.sleep;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import no.javazone.LongRunningTask;
import no.javazone.StatsPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class VertxSleepExample {
    public static void main(String[] args) throws InterruptedException {
        VertxOptions options = new VertxOptions();
        options.setEventLoopPoolSize(100);
        Vertx vertx = Vertx.vertx(options);
        EventBus eb = vertx.eventBus();
        CountDownLatch latch = new CountDownLatch(LongRunningTask.numRuns);
        StatsPrinter printer = new StatsPrinter(latch);
        eb.consumer("tasks", idx -> {
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });
        IntStream.range(0,LongRunningTask.numRuns).forEach(i -> eb.send("tasks",i));
        printer.print();
        vertx.close();
    }
}
