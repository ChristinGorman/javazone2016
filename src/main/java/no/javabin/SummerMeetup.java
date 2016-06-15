package no.javabin;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.util.Timer.time;

public class SummerMeetup {

    static int runCount = 10000;

    static CountDownLatch latch = new CountDownLatch(runCount);

    static Runnable task = () -> {
        Random rand = new Random();
        long num = 0;
        for (int i = 0; i < 100_000; i++) {
            num += rand.nextLong() - i;
        }
        latch.countDown();
    };

    static Runnable sleepTask = () -> {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    };

    static Runnable biTask = () -> {
        Random rand = new Random();
        BigInteger num = BigInteger.ZERO;
        for (int i = 0; i < 100_000; i++) {
            num = num.add(BigInteger.valueOf(rand.nextLong() - i));
        }
        latch.countDown();
    };

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newFixedThreadPool(2000);
        time(() -> {
            for (int i = 0; i < runCount; i++) {
                //new Thread(task).start();
                //task.run();
                //exec.submit(task);
                //biTask.run();
                //exec.submit(biTask);
                //new Thread(biTask).start();
                //sleepTask.run();
                exec.submit(sleepTask);
            }
            latch.await();
            System.out.println("Remaining: " + latch.getCount());
        });
        exec.shutdownNow();
    }
}
