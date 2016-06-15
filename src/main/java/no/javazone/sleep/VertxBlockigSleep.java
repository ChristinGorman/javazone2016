package no.javazone.sleep;

import io.vertx.core.Vertx;
import no.javazone.TaskRunner;

public class VertxBlockigSleep {


    /**
    Nope, no good
     */
    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();

        TaskRunner taskRunner = new TaskRunner(1000);
        taskRunner.runTask(()->
                vertx.executeBlocking(
                        (f) -> f.complete(Sleeper.sleep1Sec()),
                        (l) -> taskRunner.countDown()));

        vertx.close();
    }


}
