package no.javazone.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import no.javazone.TaskRunner;

import static no.javazone.RunConfig.numRuns;

public class VertxCalls {


    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient();
        TaskRunner runner = new TaskRunner(numRuns);
        runner.runTask(() ->
            client.getNow(8080, "localhost", "/", resp -> runner.countDown())
        );
        vertx.close();
    }
}
