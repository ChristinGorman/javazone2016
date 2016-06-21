package no.javazone;

import co.paralleluniverse.fibers.Suspendable;

import java.math.BigInteger;
import java.util.Random;

public class Big {

    @Suspendable
    public static long task() {
        Random random = new Random();
        long big = 1L;
        for (int j = 0; j < 100_000; j++) {
            big += random.nextLong() - j;
        }
        return big;
    }
    @Suspendable
    public static BigInteger memoryHog() {
        Random random = new Random();
        BigInteger counter = BigInteger.valueOf(0);
        for (int j = 0; j < 100_000; j++) {
            counter = counter.add(BigInteger.valueOf(j + random.nextLong()));
        }
        return counter;
    }
}
