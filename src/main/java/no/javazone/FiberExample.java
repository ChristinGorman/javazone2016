package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static no.javazone.RunConfig.url;

public class FiberExample {

    private static ObjectMapper mapper = new ObjectMapper();

    private static CloseableHttpClient client = FiberHttpClientBuilder.
        create(20).
        setMaxConnPerRoute(9999).
        setMaxConnTotal(9999).build();

    /**
     * In current state - no where near as good performance as vertx, but syntax is better
     */
    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(1_000);
        taskRunner.runTask(() ->
            new Fiber((SuspendableRunnable) () -> {
                getPersonInCity("abc");
                taskRunner.countDown();
            }).start());
    }

    @Suspendable
    private static TypicalExamples.PersonInCity getPersonInCity(String id) {
        if (!validateId(id)) {
            throw new IllegalArgumentException("invalid id");
        }
        try {
            InputStream personStream = client.execute(new HttpGet(url + "/person/" + id)).getEntity().getContent();
            String personContent = new BufferedReader(new InputStreamReader(personStream)).readLine();
            TypicalExamples.Person person = parsePerson(personContent);

            InputStream addressStream = client.execute(new HttpGet(url + "/address/" + id)).getEntity().getContent();
            String addressContent = new BufferedReader(new InputStreamReader(addressStream)).readLine();
            TypicalExamples.Address address = parseAddress(addressContent);

            return translate(person, address);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TypicalExamples.Address parseAddress(String body) throws IOException {
        return mapper.readValue(body, TypicalExamples.Address.class);
    }

    private static TypicalExamples.Person parsePerson(String body) throws IOException {
        return mapper.readValue(body, TypicalExamples.Person.class);
    }

    private static TypicalExamples.PersonInCity translate(TypicalExamples.Person p, TypicalExamples.Address a) {
        return new TypicalExamples.PersonInCity(p.name, a.city);
    }

    private static boolean validateId(String id) {
        return !(id == null || id.isEmpty());
    }
}
