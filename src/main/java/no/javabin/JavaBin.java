package no.javabin;

import no.javazone.sleep.Sleeper;
import no.javazone.util.Timer;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static no.javazone.util.Timer.time;

public class JavaBin {

    public static final int numRuns = 10_000;

    static CountDownLatch done = new CountDownLatch(numRuns);

    static Runnable task = () -> {
        /*Random rnd = new Random();
        long num = 0;
        for (int i = 0; i < 100_000; i++) {
            num += rnd.nextLong() - i;
        }*/
        Sleeper.sleep1Sec();
        done.countDown();
    };

    public static void main(String[] args) throws Exception {
        time(() -> {
            ExecutorService exec = Executors.newFixedThreadPool(2000);
            for (int i = 0; i < numRuns; i++) {
                exec.submit(task);
            }
            done.await(10, TimeUnit.SECONDS);
            System.out.println("Remaining: " + done.getCount());
            exec.shutdownNow();
        });
    }
}
