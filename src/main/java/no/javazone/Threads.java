package no.javazone;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.numRuns;
import static no.javazone.LongRunningTask.task;

public class Threads {

    public static void main(String[] args) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(numRuns);
        StatsPrinter printer = new StatsPrinter(countDownLatch);
        IntStream.range(0,numRuns).forEach(i->new Thread(() -> task(countDownLatch)).start());
        printer.print();
    }

}
