package no.javazone.fedex4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.IntStream.*;
import static java.util.stream.Collectors.*;

public class FedEx4J {


    /**
     * The courier organizes packages in boxes for each customer
     * Each box has the recipient and a collection of packages to deliver
     */
    static class Shelf {
        final Customer customer;
        final BlockingQueue<Package> packages;

        Shelf(Customer customer, BlockingQueue<Package> packages) {
            this.customer = customer;
            this.packages = packages;
        }

    }

    static class Courier implements Runnable {

        Thread myThread;
        final FedEx4J distributionHub;

        Courier(FedEx4J distributionHub) {
            this.distributionHub = distributionHub;
        }


        @Override
        public void run() {
            myThread = Thread.currentThread();
            while (!Thread.currentThread().isInterrupted()) {
                Shelf shelf = distributionHub.nextInLine();
                if (shelf != EMPTY_SHELF) {
                    shelf.packages.forEach(p -> p.to.onMessage(p));
                    distributionHub.inLine.put(shelf.customer, true);
                }
            }
        }

        public Courier startShift() {
            myThread = new Thread(this);
            myThread.start();
            return this;
        }

        public void endShift() {
            myThread.interrupt();
        }
    }

    static int processorCount = Runtime.getRuntime().availableProcessors();
    final List<Courier> couriers = new CopyOnWriteArrayList<>();

    final Map<Customer, Shelf> shelves = new ConcurrentHashMap<>();
    final ConcurrentMap<Customer, Boolean> inLine = new ConcurrentHashMap<>();

    private FedEx4J() {

    }

    public static FedEx4J create() {
        FedEx4J fedEx4J = new FedEx4J();
        fedEx4J.couriers.addAll(range(0, processorCount)
                .mapToObj(i -> new Courier(fedEx4J).startShift())
                .collect(toList()));
        return fedEx4J;
    }

    private static Shelf EMPTY_SHELF = new Shelf(null, new LinkedBlockingQueue<>());
    public Shelf nextInLine() {
        if (shelves.isEmpty()) return EMPTY_SHELF;

        BlockingQueue<Package> forCourier = new LinkedBlockingQueue<>();
        Optional<Customer> customer = inLine.entrySet().stream()
                .filter(e -> e.getValue())
                .map(e -> e.getKey())
                .findAny();
        if (customer.isPresent() && inLine.put(customer.get(), false)) {
            shelves.get(customer.get()).packages.drainTo(forCourier);
            return new Shelf(customer.get(), forCourier);
        }
        return EMPTY_SHELF;
    }

    public void sendPackage(Package pkg) {

        if (!shelves.containsKey(pkg.to)) {
            shelves.putIfAbsent(pkg.to, new Shelf(pkg.to, new LinkedBlockingQueue<>()));
        }
        shelves.get(pkg.to).packages.offer(pkg);
        inLine.putIfAbsent(pkg.to, true);
    }

    public void close() {
        couriers.forEach(c -> c.endShift());
    }

}
