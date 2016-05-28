package no.javazone;

import co.paralleluniverse.fibers.Suspendable;

import java.math.BigInteger;


public class Big {

    @Suspendable
    public static BigInteger task() {
        BigInteger big = BigInteger.valueOf(1l);
        for (int j = 0; j < 250_000; j++) {
            big = big.add(BigInteger.valueOf(250_000 - j));
        }
        return big;
    }
}
