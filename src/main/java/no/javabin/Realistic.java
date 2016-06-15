package no.javabin;

import no.javazone.sleep.Sleeper;
import rx.Observable;

import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static no.javazone.util.Timer.time;

public class Realistic {

    static ExecutorService exec = Executors.newFixedThreadPool(2);

    public static Integer ageWebService() {
        Sleeper.sleep1Sec();
        return new Random().nextInt(100);
    }

    public static String nameWebService() {
        Sleeper.sleep1Sec();
        return "javaBin";
    }


    public static void main(String[] args) throws Exception {
        time(() -> {
            //System.out.println("Result: " + serial());
            //System.out.println("Result: " + futures());
            //System.out.println("Result: " + completableFutures());
            rxJava().subscribe(result -> System.out.println("Result: " + result));
        });
        exec.shutdownNow();
    }

    private static Observable<String> rxJava() {
        Observable<Integer> age = Observable.from(exec.submit(Realistic::ageWebService));
        Observable<String> name = Observable.from(exec.submit(Realistic::nameWebService));

        return Observable.zip(name, age, (n, a) -> n + a);
    }

    private static String completableFutures() throws Exception {
        return supplyAsync(Realistic::ageWebService)
            .thenCombine(
                supplyAsync(Realistic::nameWebService),
                (a, n) -> n + a
            )
            .get();
    }

    private static String futures() throws Exception {
        Future<Integer> age = exec.submit(Realistic::ageWebService);
        Future<String> name = exec.submit(Realistic::nameWebService);

        String result = name.get() + age.get();

        return result;
    }

    private static String serial() {
        return nameWebService() + ageWebService();
    }
}
