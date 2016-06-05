package no.javazone;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import no.javazone.util.Timer;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static no.javazone.util.Timer.time;

public class Akka {

    static class TaskActor extends UntypedActor {

        @Override
        public void onReceive(Object msg) throws Exception {
            if ("run".equals(msg)) {
                Long result = Big.task();

//                System.out.println(self().path() + " Made result: " + result);

                sender().tell(result, self());
            } else {
                unhandled(msg);
            }
        }
    }

    static class TaskRunner extends UntypedActor {

        private ActorRef taskActor;
        private ActorRef originator;

        private int activeTasks = 0;

        public TaskRunner() {
            this.taskActor =
                    context().actorOf(Props
                                    .create(TaskActor.class)
                                    .withRouter(new RoundRobinPool(10))
                            ,
                            "task"
                    );
        }

        @Override
        public void onReceive(Object msg) throws Exception {
            if ("start".equals(msg)) {
                for (int i = 0; i < RunConfig.numRuns; i++) {
                    taskActor.tell("run", self());
                    activeTasks++;
                }
                originator = sender();
            } else if (msg instanceof Long) {
//                System.out.println(self().path() + " Result ready: " + msg);

                activeTasks--;
                if (activeTasks == 0) {
                    originator.tell("done", self());
                } else if (activeTasks % 1000 == 0) {
                    System.out.println("Remaining: " + activeTasks);
                }
            } else {
                unhandled(msg);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create();

        time(() -> {
            ActorRef taskRunner = system.actorOf(Props.create(TaskRunner.class), "runner");

            Await.result(Patterns.ask(taskRunner, "start", 30000), Duration.create(30, TimeUnit.SECONDS));
        });
        system.shutdown();
    }
}
