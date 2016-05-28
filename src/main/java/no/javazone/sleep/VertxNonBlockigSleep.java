package no.javazone.sleep;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import no.javazone.LongRunningTask;
import no.javazone.StatsPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class VertxNonBlockigSleep {
    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(100));
        EventBus eb = vertx.eventBus();
        CountDownLatch latch = new CountDownLatch(LongRunningTask.numRuns);
        StatsPrinter printer = new StatsPrinter(latch);
        eb.consumer("tasks", idx -> {
            NonBlockingSleeper.sleep(1000);
            latch.countDown();
        });
        IntStream.range(0,LongRunningTask.numRuns).forEach(i -> eb.send("tasks",i));
        printer.print();
        vertx.close();
    }
}
