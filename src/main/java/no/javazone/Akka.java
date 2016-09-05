package no.javazone;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static no.javazone.util.Timer.time;

public class Akka {

    private static class TaskActor extends AbstractActor {

        public TaskActor() {
            receive(
                ReceiveBuilder
                    .matchEquals("run", msg -> sender().tell(Big.task(), self()))
                    .build()
            );
        }
    }

    private static class TaskRunner extends AbstractActor {

        private ActorRef taskActor;
        private ActorRef originator;

        private int activeTasks = 0;
        private long currentSum = 0;

        public TaskRunner() {
            this.taskActor =
                context().actorOf(
                    Props.create(TaskActor.class).withRouter(new RoundRobinPool(10)),
                    "task"
                );

            receive(
                ReceiveBuilder
                    .matchEquals("start", msg -> {
                        for (int i = 0; i < RunConfig.numRuns; i++) {
                            taskActor.tell("run", self());
                            activeTasks++;
                        }
                        originator = sender();
                    })
                    .match(Long.class, msg -> {
                        currentSum += msg;
                        activeTasks--;
                        if (activeTasks == 0) {
                            originator.tell(Objects.hashCode(currentSum), self());
                        } else if (activeTasks % 1000 == 0) {
                            System.out.println("Remaining: " + activeTasks + " current: " + Objects.hashCode(currentSum));
                        }
                    })
                    .build()
            );
        }
    }

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create();

        time(() -> {
            ActorRef taskRunner = system.actorOf(Props.create(TaskRunner.class), "runner");

            Object result = Await.result(Patterns.ask(taskRunner, "start", 30000), Duration.create(30, TimeUnit.SECONDS));

            System.out.println("Done result: " + result);
        });
        system.shutdown();
    }
}
