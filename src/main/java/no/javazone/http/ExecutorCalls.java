package no.javazone.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.url;

public class ExecutorCalls {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TaskRunner runner = new TaskRunner(1000);

        runner.runTask(() -> executor.submit(runner.trackRunnable(() -> {
            try {
                CloseableHttpClient client = HttpClientBuilder.create().build();
                HttpUriRequest request = new HttpGet(url);
                return client.execute(request).getStatusLine().getStatusCode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })));
    }
}
