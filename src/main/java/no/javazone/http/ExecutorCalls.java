package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static no.javazone.RunConfig.url;

public class ExecutorCalls {

    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(50).build();

    /**
    meh
     */
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        Supplier task = () -> {
            try (CloseableHttpResponse response = client.execute(new HttpGet(url + "/person/a"))){
                return response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

        new TaskRunner(10_000, task).runOnExecutor(executor);
    }
}
