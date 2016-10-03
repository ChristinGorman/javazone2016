package no.javazone.fedex4j;

import java.util.*;
import java.util.concurrent.*;

import static java.util.stream.IntStream.*;
import static java.util.stream.Collectors.*;

public class FedEx4J {



    /**
     * The courier organizes packages in boxes for each customer
     * Each box has the recipient and a collection of packages to deliver
     */
    static class BoxWithPackages {
        final Office recipient;
        final BlockingQueue<Package> packages;

        BoxWithPackages(Office office, BlockingQueue<Package> packages) {
            this.recipient = office;
            this.packages = packages;
        }

    }

    static class DroneCourier implements Runnable {

        Thread myThread;
        final FedEx4J distributionHub;

        DroneCourier(FedEx4J distributionHub) {
            this.distributionHub = distributionHub;
        }


        @Override
        public void run() {
            myThread = Thread.currentThread();
            while (!Thread.currentThread().isInterrupted()) {
                distributionHub.getNextPackagesToDeliver()
                        .packages.forEach(p -> p.to.onMessage(p));
            }
        }


        public DroneCourier startShift() {
            new Thread(this).start();
            return this;
        }

        public void endShift() {
            myThread.interrupt();
        }
    }


    static int processorCount = Runtime.getRuntime().availableProcessors();
    final List<DroneCourier> couriers = new CopyOnWriteArrayList<>();

    final Map<Office, BoxWithPackages> shelves = new ConcurrentHashMap<>();


    private FedEx4J() {

    }

    public static FedEx4J create() {
        FedEx4J fedEx4J = new FedEx4J();
        fedEx4J.couriers.addAll(range(0, processorCount)
                .mapToObj(i -> new DroneCourier(fedEx4J).startShift())
                .collect(toList()));
        return fedEx4J;
    }

    private static BoxWithPackages EMPTY_BOX = new BoxWithPackages(null, new LinkedBlockingQueue<>());

    public BoxWithPackages getNextPackagesToDeliver() {
        if (shelves.isEmpty()) return EMPTY_BOX;

        BlockingQueue<Package> forCourier = new LinkedBlockingQueue<>();
        Optional<BoxWithPackages> box = shelves.values().stream().filter(e -> !e.packages.isEmpty()).findAny();
        if (box.isPresent()) {
            box.get().packages.drainTo(forCourier);
            return new BoxWithPackages(box.get().recipient, forCourier);
        }
        return EMPTY_BOX;
    }

    public void sendPackage(Package pkg) {
        if (!shelves.containsKey(pkg.to)) {
            shelves.putIfAbsent(pkg.to, new BoxWithPackages(pkg.to, new LinkedBlockingQueue<>()));
        }
        shelves.get(pkg.to).packages.offer(pkg);
    }

    public void close() {
        couriers.forEach(c -> c.endShift());
    }

}
