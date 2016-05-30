package no.javazone.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;
import static no.javazone.RunConfig.url;

public class ExecutorCalls {

    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(500).build();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 3);
        TaskRunner runner = new TaskRunner(numRuns);

        runner.runTask(() -> executor.submit(() -> {
            try {
                client.execute(new HttpGet(url)).close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                runner.countDown();
            }
        }));
        executor.shutdownNow();
    }
}
