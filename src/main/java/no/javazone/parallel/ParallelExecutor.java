package no.javazone.parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static no.javazone.util.Timer.time;

public class ParallelExecutor {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        time(() -> {
            Future<String> strResult = executor.submit(Tasks::sleepStr);

            Future<Integer> intResult = executor.submit(Tasks::sleepRandInt);

            System.out.println("Got result: " + strResult.get() + "#" + intResult.get());
        });

        System.exit(0);
    }
}
