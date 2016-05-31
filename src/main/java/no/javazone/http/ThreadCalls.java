package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static no.javazone.RunConfig.numRuns;
import static no.javazone.RunConfig.url;

public class ThreadCalls {
    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(50).build();

    /**
    Not great and obviously limited by the number of threads the system  memory can support
     */
    public static void main(String[] args) throws InterruptedException {
        TaskRunner runner = new TaskRunner(numRuns);
        runner.runTask(()->new Thread(() -> {
            try {
                client.execute(new HttpGet(url)).close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                runner.countDown();
            }
        }).start());
    }
}
