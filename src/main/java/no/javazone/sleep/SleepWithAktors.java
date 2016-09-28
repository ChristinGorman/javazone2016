package no.javazone.sleep;

import no.javazone.TaskRunner;
import no.javazone.fedex4j.Customer;
import no.javazone.fedex4j.FedEx4J;
import no.javazone.fedex4j.Package;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SleepWithAktors {

    public static FedEx4J fedEx4J = FedEx4J.create();

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


    static class Delegator<T> implements Customer<T> {

        final int count;
        final Queue<Customer<T>> myCustomers = new ConcurrentLinkedQueue<>();

        Delegator(int count, Supplier<Customer<T>> customer) {
            this.count = count;
            IntStream.range(0, count).forEach(i-> myCustomers.offer(customer.get()));
        }

        @Override
        public void onMessage(Package<T> msg) {
            Customer<T> thisOne = myCustomers.poll();
            thisOne.onMessage(msg);
            myCustomers.offer(thisOne);
        }
    }

    static class SleepMessage {
        final long timestamp;

        SleepMessage(long sleepDuration) {
            this.timestamp = System.currentTimeMillis() + sleepDuration;
        }

    }

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(10_000);

        Customer<SleepMessage> delegator = new Delegator<>(10, ()->new SleepingCustomer());

        Customer from = msg -> taskRunner.countDown();

        Package<SleepMessage> pkg = new Package(delegator, new SleepMessage(1000), from);
        taskRunner.runTask(() -> fedEx4J.sendPackage(pkg));

        fedEx4J.close();
    }
}
