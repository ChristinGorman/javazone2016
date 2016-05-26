package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class FiberSleep {
    public static void main(String[] args) throws Exception {
        AtomicInteger succeededcount = new AtomicInteger(0);
        long t = System.currentTimeMillis();
        int num = 1_000_000;
        IntStream.range(0, num).forEach(i -> new Fiber<>("ECHO", (SuspendableRunnable) () -> {
            Fiber.sleep(1000);
            succeededcount.incrementAndGet();
        }).start());
        while (succeededcount.get() < num){ Thread.yield();}
        System.out.println((System.currentTimeMillis() - t) + " succeeded count " + succeededcount.get());
    }
}
