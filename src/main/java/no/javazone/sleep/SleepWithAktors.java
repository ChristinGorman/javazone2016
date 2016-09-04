package no.javazone.sleep;

import no.javazone.TaskRunner;
import no.javazone.fedex4j.Customer;
import no.javazone.fedex4j.FedEx4J;
import no.javazone.fedex4j.Package;

public class SleepWithAktors {

    public static FedEx4J fedEx4J = new FedEx4J();

    static class SleepingCustomer implements Customer<SleepMessage> {

        @Override
        public void onMessage(Package<SleepMessage> message) {
            if (doneSleeping(message.payload)) {
                fedEx4J.sendPackage(new Package(message.from, "done", this));
            } else {
                fedEx4J.sendPackage(message);
            }
        }

        boolean doneSleeping(SleepMessage msg) {
            return System.currentTimeMillis() >= msg.timestamp;
        }
    }

    static class SleepMessage {
        final long timestamp;

        SleepMessage(long sleepDuration) {
            this.timestamp = System.currentTimeMillis() + sleepDuration;
        }

    }

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(5_000_000);

        Customer<SleepMessage> to = fedEx4J.addCustomer(new SleepingCustomer());
        Customer from = fedEx4J.addCustomer(msg -> taskRunner.countDown());

        Package<SleepMessage> pkg = new Package(to, new SleepMessage(1000), from);
        taskRunner.runTask(() -> fedEx4J.sendPackage(pkg));

        fedEx4J.close();
    }
}
