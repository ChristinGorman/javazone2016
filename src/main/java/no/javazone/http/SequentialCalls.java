package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class SequentialCalls {
    public static void main(String[] args) throws InterruptedException {
        String url = "http://localhost:8080";
        TaskRunner runner = new TaskRunner(1000);
        runner.runTask(runner.trackRunnable(() -> {
            try {
                CloseableHttpClient client = HttpClientBuilder.create().build();
                HttpUriRequest request = new HttpGet(url);
                return client.execute(request).getStatusLine().getStatusCode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
