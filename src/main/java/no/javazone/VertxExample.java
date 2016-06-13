package no.javazone;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

public class VertxExample {


    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();

        HttpClient httpClient = vertx.createHttpClient();

        String id = "abc";

        if (!validateId(id)) {
            throw new IllegalArgumentException("ugyldig id");
        }

        Future<TypicalExamples.Person> personFuture = Future.future();
        httpClient.getNow(8080, "localhost", "/", r -> personFuture.complete(new TypicalExamples.Person(id, "someone", 37)));

        Future<TypicalExamples.Address> addressFuture = Future.future();
        httpClient.getNow(8080, "localhost", "/", r -> addressFuture.complete(new TypicalExamples.Address(id, "hus 2", "gate 2", "1337", "Sandvika")));

        CompositeFuture.all(personFuture, addressFuture).setHandler(result -> {
            if (result.succeeded()) {
                //TODO få tak i resultatene - hvor i alle dager er de?
                System.out.println("Hurra! men hvor er resultatene mine...");
            } else {
                result.cause().printStackTrace();
            }
            vertx.close();
        });

    }

    private static boolean validateId(String id) {
        return !(id == null || id.isEmpty());
    }


}
