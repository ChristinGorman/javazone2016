package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class VertxExample {
    public static void main(String[] args) throws InterruptedException {
        long t = System.currentTimeMillis();
        VertxOptions options = new VertxOptions();
        options.setEventLoopPoolSize(100);
        Vertx vertx = Vertx.vertx(options);
        EventBus eb = vertx.eventBus();
        CountDownLatch latch = new CountDownLatch(LongRunningTask.numRuns);
        eb.consumer("tasks", idx -> LongRunningTask.task(latch));
        IntStream.range(0,LongRunningTask.numRuns).forEach(i -> eb.send("tasks",i));
        latch.await(15, TimeUnit.SECONDS);
        System.out.println("vertx " + StatsPrinter.stats(t, latch.getCount()));
        vertx.close();
    }
}
