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
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static no.javazone.util.Timer.time;

public class Akka {

    private static final int numRuns = 10_000;

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
                            originator.tell(currentSum, self());
                        }
                    })
                    .matchEquals("progress", msg -> printProgress(activeTasks))
                    .build()
            );
        }
    }

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create();

        time(() -> {
            printProgress(numRuns);
            ActorRef taskRunner = system.actorOf(Props.create(TaskRunner.class), "runner");

            FiniteDuration duration = Duration.create(100, TimeUnit.MILLISECONDS);
            system.scheduler().schedule(
                duration,
                duration,
                taskRunner,
                "progress",
                system.dispatcher(),
                ActorRef.noSender()
            );

            Object result = Await.result(
                Patterns.ask(taskRunner, "start", 30000),
                Duration.create(30, TimeUnit.SECONDS)
            );

            printProgress(0);
            System.out.println("\nDone result: " + result);
        });
        system.shutdown();
    }

    private static void printProgress(int activeTasks) {
        int mem = (int) ((Runtime.getRuntime().totalMemory() * 100) / Runtime.getRuntime().maxMemory());
        long progress = ((numRuns - activeTasks) * 100) / numRuns;
        String line = String.format(
            "\r(memory: %d%%) (progress: %d%%) (active threads: %d)",
            mem, progress, Thread.activeCount()
        );
        System.out.print(line);
        System.out.flush();
    }
}
