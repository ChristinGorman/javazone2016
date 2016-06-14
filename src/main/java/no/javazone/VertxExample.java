package no.javazone;


import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;

public class VertxExample {

    static Vertx vertx = Vertx.vertx();
    static HttpClient httpClient = vertx.createHttpClient();

    public static void main(String[] args) throws Exception {
        getPersonInCity("abc", result -> {
            if (result.succeeded()) {
                System.out.println(result.result());
            }else {
                result.cause().printStackTrace();
            }
            vertx.close();
        });

    }

    public static void getPersonInCity(String id, Handler<AsyncResult<TypicalExamples.PersonInCity>> handler) {
        if (!validateId(id)) {
            handler.handle(Future.failedFuture(new IllegalArgumentException("ugyldig id")));
        }

        Future<TypicalExamples.Person> personFuture = Future.future();
        httpClient.getNow(8080, "localhost", "/person/" + id, r -> r.bodyHandler(body -> personFuture.complete(parsePerson(body))));

        Future<TypicalExamples.Address> addressFuture = Future.future();
        httpClient.getNow(8080, "localhost", "/address/" + id, r -> r.bodyHandler(body -> addressFuture.complete(parseAddress(body))));

        CompositeFuture.all(personFuture, addressFuture).setHandler(result -> {
            if (result.succeeded()) {
                handler.handle(Future.succeededFuture(translate(personFuture.result(), addressFuture.result())));
            } else {
                handler.handle(Future.failedFuture(result.cause()));
            }
        });
    }

    private static TypicalExamples.Address parseAddress(Buffer body) {
        return Json.decodeValue(body.getString(0, body.length()), TypicalExamples.Address.class);
    }

    private static TypicalExamples.Person parsePerson(Buffer body) {
        return Json.decodeValue(body.getString(0, body.length()), TypicalExamples.Person.class);
    }

    private static TypicalExamples.PersonInCity translate(TypicalExamples.Person p, TypicalExamples.Address a){
        return new TypicalExamples.PersonInCity(p.name, a.city);
    }

    private static boolean validateId(String id) {
        return !(id == null || id.isEmpty());
    }


}
