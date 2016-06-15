package no.javazone.http;

import no.javazone.TaskRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.function.Supplier;

import static no.javazone.RunConfig.url;

public class SequentialCalls {

    static CloseableHttpClient client = HttpClientBuilder.
            create().
            setMaxConnPerRoute(50).
            setMaxConnTotal(50).build();

    /**
    Not even worth running.
     */
    public static void main(String[] args) throws Exception {
        Supplier task = ()-> {
            try(CloseableHttpResponse response = client.execute(new HttpGet(url))) {
                return response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        new TaskRunner(100, task).run();
    }
}
