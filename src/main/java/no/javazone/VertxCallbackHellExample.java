package no.javazone;


import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;

public class VertxCallbackHellExample {

    static Vertx vertx = Vertx.vertx();
    static HttpClient httpClient = vertx.createHttpClient();

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(100_000);
        taskRunner.runTask(() -> getPersonInCity("abc", result -> {
            if (result.succeeded()) {
                //System.out.println(result.result());
            } else {
                result.cause().printStackTrace();
            }
            taskRunner.countDown();
        }));
        vertx.close();
    }

    public static void getPersonInCity(String id, Handler<AsyncResult<TypicalExamples.PersonInCity>> handler) {
        if (!validateId(id)) {
            handler.handle(Future.failedFuture(new IllegalArgumentException("ugyldig id")));
        }

        httpClient.getNow(8080, "localhost", "/person/" + id, personResponse -> {
            personResponse.bodyHandler(
                    personBody -> {
                        TypicalExamples.Person person = parsePerson(personBody);
                        httpClient.getNow(8080, "localhost", "/address/" + id, addressResponse -> addressResponse.bodyHandler(
                                addressBody -> {
                                    TypicalExamples.Address address = parseAddress(addressBody);
                                    handler.handle(Future.succeededFuture(translate(person, address)));
                                }));
                    });
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
