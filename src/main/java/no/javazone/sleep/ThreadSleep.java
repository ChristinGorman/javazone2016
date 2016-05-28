package no.javazone.sleep;

import no.javazone.LongRunningTask;
import no.javazone.StatsPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class ThreadSleep {

    public static void main(String[] args) throws Exception {
        CountDownLatch done =new CountDownLatch(LongRunningTask.numRuns);
        StatsPrinter printer =new StatsPrinter(done);
        IntStream.range(0, LongRunningTask.numRuns).forEach(i -> new Thread(() -> {
            try {
                Thread.sleep(LongRunningTask.numRuns);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            done.countDown();
        }).start());
        printer.print();
    }
}
