package no.javazone;

import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.numRuns;

public class Sequence {

    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        IntStream.range(0,numRuns).forEach(i-> LongRunningTask.task());
        System.out.println("sequence: " + StatsPrinter.stats(t, 0));
    }

}
