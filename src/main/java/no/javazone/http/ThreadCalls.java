package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class ThreadCalls {
    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(500).build();
    public static void main(String[] args) throws InterruptedException {
        String url = "http://localhost:8080";
        TaskRunner runner = new TaskRunner(1000);
        runner.runTask(()->new Thread(runner.trackRunnable(() -> {
            try {
                HttpUriRequest request = new HttpGet(url);
                return client.execute(request).getStatusLine().getStatusCode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })).start());
    }
}
