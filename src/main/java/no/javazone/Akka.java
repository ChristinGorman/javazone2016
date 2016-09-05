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
import java.util.stream.IntStream;

import static no.javazone.util.Timer.time;

public class Akka {

    private static final int numRuns = 50_000;

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
                        for (int i = 0; i < numRuns; i++) {
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
                            printProgress(activeTasks);
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

            printProgress(0);
            System.out.println("\nDone result: " + result);
        });
        system.shutdown();
    }

    private static void printProgress(int activeTasks) {
        int mem = (int) ((Runtime.getRuntime().totalMemory() * 100) / Runtime.getRuntime().maxMemory());
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, mem / 2).forEach(i -> builder.append('\u2588'));
        builder.append(" ").append(mem).append("%");
        IntStream.range(Math.max(0, 50 - (50 - builder.length())), 50).forEach(i -> builder.append(" "));
        long progress = ((numRuns - activeTasks) * 100) / numRuns;
        String line = "\rMemory usage: " + builder.toString() + " (progress: " + progress + "%)";
        System.out.print(line);
        System.out.flush();
    }
}
