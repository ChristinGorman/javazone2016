package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.Metrics;

import java.util.stream.IntStream;

public class FiberSleep {
    public static void main(String[] args) throws Exception {
        int num = 1000_000;
        Metrics printer = new Metrics();
        IntStream.range(0, num).forEach(i -> new Fiber<>("ECHO", (SuspendableRunnable) () -> {
            Fiber.sleep(1000);
            printer.countDown();
        }).start());
        printer.print();
    }
}
