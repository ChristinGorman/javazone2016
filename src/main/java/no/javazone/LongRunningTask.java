package no.javazone;

import co.paralleluniverse.fibers.Suspendable;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class LongRunningTask {

    private static AtomicInteger count = new AtomicInteger(0);
    public static int numRuns = 2000;

    public static BigInteger task() {
        return task(new CountDownLatch(1));
    }

    @Suspendable
    public static BigInteger task(CountDownLatch done) {
        BigInteger big = new BigInteger("1");
        for (int j = 0; j < 300_000; j++) {
            big = big.add(BigInteger.valueOf(300_000 - j));
        }
        done.countDown();
        return big;
    }

}
