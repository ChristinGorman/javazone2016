package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.function.Supplier;

import static no.javazone.RunConfig.url;

public class ThreadCalls {
    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(50).build();

    /**
    Not great and obviously limited by the number of threads the system  memory can support
     */
    public static void main(String[] args) throws Exception {
        Supplier task = ()-> {
            try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
                return response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        new TaskRunner(1000, task).runOnThread();
    }
}
