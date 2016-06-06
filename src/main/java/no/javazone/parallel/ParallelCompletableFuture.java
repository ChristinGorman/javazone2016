package no.javazone.parallel;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static no.javazone.util.Timer.time;

public class ParallelCompletableFuture {

    public static void main(String[] args) throws Exception {
        time(() ->
            CompletableFuture
                    .supplyAsync(() -> {
                        trySleep();
                        //throw new RuntimeException("ouch!");
                        System.out.println(Thread.currentThread().getName() + " str");
                        return "javaBin";
                    })
                    .thenCombine(
                            CompletableFuture.supplyAsync(() -> {
                                trySleep();
                                System.out.println(Thread.currentThread().getName() + " int");
                                return new Random().nextInt();
                            }),
                            (str, num) -> str + "#" + num
                    )
                    .whenComplete((str, err) -> {
                        System.out.println(Thread.currentThread().getName() + " result");
                        if (err != null) {
                            System.out.println("Error: " + err.getMessage());
                        } else {
                            System.out.println("Got result: " + str);
                        }
                    })
                    .get()

        );
    }

    private static void trySleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
