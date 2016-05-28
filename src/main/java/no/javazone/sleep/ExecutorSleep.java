package no.javazone.sleep;

import no.javazone.Metrics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ExecutorSleep {
    public static void main(String[] args) throws Exception {

        ExecutorService ex = Executors.newFixedThreadPool(2000);
        int num = 100_000;
        Metrics printer = new Metrics();
        IntStream.range(0, num).forEach(i -> ex.submit(printer.track(BlockingSleeper::sleep1Sec)));
        printer.print();
    }
}
