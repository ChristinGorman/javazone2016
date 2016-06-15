package no.javazone.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import no.javazone.TaskRunner;

public class VertxCalls {


    /**
    Sucky syntax, great performance. Millions of requests handled with no problems.
     */
    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient();
        TaskRunner runner = new TaskRunner(10_000);
        runner.runTask(() ->
            client.getNow(8080, "localhost", "/", resp -> runner.countDown())
        );
        vertx.close();
    }
}
