package no.javazone.parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static no.javazone.util.Timer.time;

public class ParallelExecutor {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        time(() -> {
            Future<String> strResult = executor.submit(() -> {
                Thread.sleep(1000);
                //throw new RuntimeException("ouch!");
                return "javaBin";
            });

            Future<Integer> intResult = executor.submit(() -> {
                Thread.sleep(1000);
                return new Random().nextInt();
            });

            System.out.println("Got result: " + strResult.get() + "#" + intResult.get());
        });

        executor.shutdownNow();
    }
}
