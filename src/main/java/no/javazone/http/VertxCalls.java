package no.javazone.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.h2.util.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static no.javazone.RunConfig.numRuns;
import static no.javazone.RunConfig.url;

public class VertxCalls {


    public static void main(String[] args) throws InterruptedException {
        HttpClient client = Vertx.vertx().createHttpClient();
        TaskRunner runner = new TaskRunner(numRuns);
        runner.runTask(() ->
            client.getNow(8080, "localhost", "/",
                    resp -> resp.bodyHandler(body -> runner.countDown()))
        );
    }
}
