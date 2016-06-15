package no.javazone.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.google.common.base.Stopwatch;
import no.javazone.TaskRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static no.javazone.RunConfig.url;

public class FiberHttpRequest {


    static CloseableHttpClient client = FiberHttpClientBuilder.
            create(20).
            setMaxConnPerRoute(9999).
            setMaxConnTotal(9999).build();

    /**
     * In current state - no where near as good performance as vertx, but syntax is better
     */
    public static void main(String[] args) throws Exception {
        int count = 9999;
        CountDownLatch done = new CountDownLatch(count);

        Stopwatch stopwatch = Stopwatch.createStarted();
        IntStream.range(0,count).forEach(i ->
            new Fiber((SuspendableRunnable) () -> {
                try (CloseableHttpResponse response = client.execute(new HttpGet(url))){
                    response.getStatusLine().getStatusCode();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    done.countDown();
                }
            }).start()
        );
        done.await(15, TimeUnit.SECONDS);
        TaskRunner.print(
                new TaskRunner.Result(stopwatch.elapsed(TimeUnit.MILLISECONDS), Runtime.getRuntime().totalMemory()),
                done.getCount(),
                FiberHttpRequest.class.getSimpleName());

    }
}
