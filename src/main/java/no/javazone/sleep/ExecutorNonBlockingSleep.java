package no.javazone.sleep;

import no.javazone.Metrics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class ExecutorNonBlockingSleep {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(2000);
        Metrics p = new Metrics();
        IntStream.range(0, numRuns).forEach(i -> ex.submit(p.track(NonBlockingSleeper::sleep1Sec)));
        p.print();
    }
}
