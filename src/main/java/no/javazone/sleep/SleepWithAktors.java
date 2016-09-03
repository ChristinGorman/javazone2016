package no.javazone.sleep;

import no.javazone.ActorSleeper;
import no.javazone.TaskRunner;

public class SleepWithAktors {

    public static class SleepingActor implements ActorSleeper.Aktor<Long> {

        @Override
        public void onMessage(ActorSleeper.ActorMessage<Long> message) {
            if (System.currentTimeMillis() >= message.message) {
                ActorSleeper.AktorSystem.get().sendMessage(message.sender, new ActorSleeper.ActorMessage("done", this));
            } else {
                ActorSleeper.AktorSystem.get().sendMessage(this, message);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(1_000_000);

        ActorSleeper.AktorSystem aktorSystem = ActorSleeper.AktorSystem.get();
        aktorSystem.addActors(() -> new SleepingActor(), Runtime.getRuntime().availableProcessors());
        ActorSleeper.Aktor<String> callback = msg -> taskRunner.countDown();
        ActorSleeper.AktorSystem.get().addActor(callback);
        ActorSleeper.ActorMessage sleepMsg = new ActorSleeper.ActorMessage(System.currentTimeMillis() + 1000, callback);
        taskRunner.runTask(() -> aktorSystem.sendMessage(SleepingActor.class, sleepMsg));
        aktorSystem.shutDown();
        System.exit(0);
    }
}
