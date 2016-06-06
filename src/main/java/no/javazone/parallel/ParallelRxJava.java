package no.javazone.parallel;

import rx.Observable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.util.Timer.time;

public class ParallelRxJava {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        time(() -> {
            Observable<String> strResult = Observable.from(executor.submit(Tasks::sleepStr));

            Observable<Integer> intResult = Observable.from(executor.submit(Tasks::sleepRandInt));

            Observable<String> result = Observable.zip(
                    strResult,
                    intResult,
                    (str, num) -> str + "#" + num
            );

            result.subscribe(str -> System.out.println("Got result: " + str));
        });

        System.exit(0);
    }
}
