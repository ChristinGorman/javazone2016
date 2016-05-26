package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ThreadSleep {
    public static void main(String[] args) throws Exception {
        AtomicInteger succeededcount = new AtomicInteger(0);
        long t = System.currentTimeMillis();
        int num = 1000;
        IntStream.range(0, num).forEach(i -> new Thread(() -> {
            try {
                Thread.sleep(num);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            succeededcount.incrementAndGet();
        }).start());
        while (succeededcount.get() < 10_000){ Thread.yield();}
        System.out.println((System.currentTimeMillis() - t) + " succeeded count " + succeededcount.get());
    }
}
