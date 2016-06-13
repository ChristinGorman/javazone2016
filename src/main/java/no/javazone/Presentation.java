package no.javazone;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Presentation {
    static int numTasks = 1000;

    static ExecutorService service = Executors.newFixedThreadPool(8);

    static CountDownLatch done = new CountDownLatch(numTasks);

    static Runnable task = () -> {
        Random rnd = new Random();
        long num = 0;

        for (int i = 0; i < 1_000_000; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            num += rnd.nextLong();
        }
        done.countDown();
    };

    static Runnable bitask = () -> {
        Random rnd = new Random();
        BigInteger num = BigInteger.valueOf(0);

        for (int i = 0; i < 500_000; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            num = num.add(BigInteger.valueOf(rnd.nextLong()));
        }
        done.countDown();
    };

    public static void main(String[] args) throws InterruptedException {

        long time = System.currentTimeMillis();

        for (int i = 0; i < numTasks; i++) {
            service.submit(task);
        }
        done.await(25, TimeUnit.SECONDS);

        System.out.println((System.currentTimeMillis() - time));
        System.out.println("remaining " + done.getCount());
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);
    }
}
