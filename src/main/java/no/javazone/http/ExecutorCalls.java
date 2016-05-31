package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static no.javazone.RunConfig.numRuns;
import static no.javazone.RunConfig.url;

public class ExecutorCalls {

    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(50).build();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
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
