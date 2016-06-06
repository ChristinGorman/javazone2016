package no.javazone.parallel;

import java.util.concurrent.CompletableFuture;

import static no.javazone.util.Timer.time;

public class ParallelCompletableFuture {

    public static void main(String[] args) throws Exception {
        time(() ->
            CompletableFuture
                    .supplyAsync(Tasks::sleepStr)
                    .thenCombine(
                            CompletableFuture.supplyAsync(Tasks::sleepRandInt),
                            (str, num) -> str + "#" + num
                    )
                    .whenComplete((str, err) -> {
                        if (err != null) {
                            System.out.println("Error: " + err.getMessage());
                        } else {
                            System.out.println("Got result: " + str);
                        }
                    })
                    .get()

        );
    }
}
