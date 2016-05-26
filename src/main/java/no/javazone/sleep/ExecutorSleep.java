package no.javazone.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ExecutorSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 5);
        AtomicInteger succeededcount = new AtomicInteger(0);
        long t = System.currentTimeMillis();
        int num = 10_000;
        IntStream.range(0, num).forEach(i -> ex.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            succeededcount.incrementAndGet();
        }));
        while (succeededcount.get() < num){ Thread.yield();}
        System.out.println((System.currentTimeMillis() - t) + " succeeded count " + succeededcount.get());
    }
}
