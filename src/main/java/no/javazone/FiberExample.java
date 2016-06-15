package no.javazone;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableRunnable;
import io.vertx.core.json.Json;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static no.javazone.RunConfig.url;

public class FiberExample {

    static CloseableHttpClient client = FiberHttpClientBuilder.
            create(20).
            setMaxConnPerRoute(9999).
            setMaxConnTotal(9999).build();

    /**
     * In current state - no where near as good performance as vertx, but syntax is better
     */
    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(1000);
        taskRunner.runTask(()->{
           new Fiber((SuspendableRunnable) () -> {
               getPersonInCity("abc");
               taskRunner.countDown();
           }).start();
        });
    }


    @Suspendable
    private static TypicalExamples.PersonInCity getPersonInCity(String id) {
        if (!validateId(id)) {
            throw new IllegalArgumentException("invalid id");
        }
        try {
            String personContent = new BufferedReader(new InputStreamReader(client.execute(new HttpGet(url + "/person/" + id)).getEntity().getContent())).readLine();
            TypicalExamples.Person person = parsePerson(personContent);

            String addressContent = new BufferedReader(new InputStreamReader((client.execute(new HttpGet(url + "/address/" + id)).getEntity().getContent()))).readLine();
            TypicalExamples.Address address = parseAddress(addressContent);

            return translate(person, address);

        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static TypicalExamples.Address parseAddress(String body) {
        return Json.decodeValue(body, TypicalExamples.Address.class);
    }

    private static TypicalExamples.Person parsePerson(String body) {
        return Json.decodeValue(body, TypicalExamples.Person.class);
    }

    private static TypicalExamples.PersonInCity translate(TypicalExamples.Person p, TypicalExamples.Address a){
        return new TypicalExamples.PersonInCity(p.name, a.city);
    }

    private static boolean validateId(String id) {
        return !(id == null || id.isEmpty());
    }
}
