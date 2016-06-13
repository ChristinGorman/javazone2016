package no.javazone.http;

import io.vertx.core.Vertx;

public class VertxHttpServer {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
    }
}
