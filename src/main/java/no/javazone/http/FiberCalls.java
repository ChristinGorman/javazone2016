package no.javazone.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import static no.javazone.RunConfig.numRuns;
import static no.javazone.RunConfig.url;

public class FiberCalls {

    static CloseableHttpClient client = FiberHttpClientBuilder.
            create(20).
            setMaxConnPerRoute(1000).
            setMaxConnTotal(1000).build();

    public static void main(String[] args) throws InterruptedException {
        TaskRunner runner = new TaskRunner(numRuns);
        runner.runTask(() -> new Fiber((SuspendableRunnable) () -> {
            try {
                client.execute(new HttpGet(url));
            } catch (Exception e) {
                //e.printStackTrace();
            }finally {
                runner.countDown();
            }
        }).start());
    }
}
