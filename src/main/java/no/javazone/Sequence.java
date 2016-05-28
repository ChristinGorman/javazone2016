package no.javazone;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.numRuns;

public class Sequence {

    public static void main(String[] args) throws Exception{
        StatsPrinter printer = new StatsPrinter(new CountDownLatch(numRuns));
        IntStream.range(0,numRuns).forEach(i-> LongRunningTask.task());
        printer.print();
    }

}
