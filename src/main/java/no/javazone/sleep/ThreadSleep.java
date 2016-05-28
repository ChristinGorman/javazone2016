package no.javazone.sleep;

import no.javazone.StatsPrinter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadSleep {

    public static void main(String[] args) throws Exception {
        int num = 10_000;
        CountDownLatch done =new CountDownLatch(num);
        long t = System.currentTimeMillis();
        IntStream.range(0, num).forEach(i -> new Thread(() -> {
            try {
                Thread.sleep(num);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            done.countDown();
        }).start());
        done.await(15, TimeUnit.SECONDS);
        System.out.println("Threads sleeping " + StatsPrinter.stats(t, done.getCount()));
    }
}
