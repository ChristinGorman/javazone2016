package no.javazone;

import co.paralleluniverse.fibers.Fiber;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static no.javazone.LongRunningTask.*;
import static no.javazone.StatsPrinter.*;

public class Fibers {

    public static void main(String[] args) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(numRuns);
        long t = System.currentTimeMillis();
        IntStream.range(0,numRuns).forEach(i->new Fiber(() -> task(countDownLatch)).start());
        countDownLatch.await();
        System.out.println("");
        System.out.println("fibers: " + stats(t, countDownLatch.getCount()));
    }

}
