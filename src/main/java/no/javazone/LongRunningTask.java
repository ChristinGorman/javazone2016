package no.javazone;

import co.paralleluniverse.fibers.Suspendable;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class LongRunningTask {

    private static AtomicInteger count = new AtomicInteger(0);
    private static BigInteger timesRecorded = new BigInteger("0");
    public static int numRuns = 2000;

    @Suspendable
    public static BigInteger task(CountDownLatch done) {
        long timestamp = System.currentTimeMillis();
        BigInteger big = new BigInteger("1");
        for (int j = 0; j < 100_000; j++) {
            big = big.add(BigInteger.valueOf(((100_000-j)%10000) * j));
        }
        done.countDown();

        timesRecorded = timesRecorded.add(BigInteger.valueOf((System.currentTimeMillis() - timestamp)));

        /*
        System.out.println("Current = " + big.toString() +
                " avg time: " + timesRecorded.divide(BigInteger.valueOf(count.incrementAndGet()))  +
                " remaining tasks : " + done.getCount());
                */
        return big;
    }




    public static String stats(long t) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        return (System.currentTimeMillis() - t) + " " + format.format(Runtime.getRuntime().totalMemory());
    }

}
