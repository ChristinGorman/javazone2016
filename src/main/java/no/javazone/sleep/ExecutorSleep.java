package no.javazone.sleep;

import no.javazone.StatsPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExecutorSleep {
    public static void main(String[] args) throws Exception {

        ExecutorService ex = Executors.newFixedThreadPool(2000);
        int num = 100_000;
        CountDownLatch done = new CountDownLatch(num);
        StatsPrinter printer = new StatsPrinter(done);
        IntStream.range(0, num).forEach(i -> ex.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            done.countDown();
        }));
        printer.print();
    }
}
