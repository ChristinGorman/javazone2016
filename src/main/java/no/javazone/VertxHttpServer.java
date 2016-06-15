package no.javazone;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class VertxHttpServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.get("/person/:id").handler(VertxHttpServer::returnPerson);
        router.get("/address/:id").handler(VertxHttpServer::returnAddress);
        router.get("/").handler(ctx -> ctx.response().end("Hello world"));
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private static void returnPerson(RoutingContext ctx) {
        String id = ctx.request().getParam("id");
        ctx.response()
                .setStatusCode(200)
                .end(Json.encode(new TypicalExamples.Person(id, "Me", 29)));
        System.out.println("requested person " + id);
    }

    private static void returnAddress(RoutingContext ctx) {
        String id = ctx.request().getParam("id");
        ctx.response()
                .setStatusCode(200)
                .end(Json.encode(new TypicalExamples.Address(id, "House 1", "Street 2", "1337", "Sandvika")));
        System.out.println("requested address " + id);
    }
}
