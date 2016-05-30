package no.javazone.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.httpclient.FiberHttpClient;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static no.javazone.RunConfig.url;

public class FiberCalls {

    static CloseableHttpClient client = FiberHttpClientBuilder.
            create(8).
            setMaxConnPerRoute(50).
            setMaxConnTotal(500).build();

    public static void main(String[] args) throws InterruptedException {
        TaskRunner runner = new TaskRunner(1000);
        runner.runTask(() -> new Fiber((SuspendableRunnable) () -> {
            try {
                client.execute(new HttpGet(url));
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                runner.countDown();
            }
        }).start());
    }
}
