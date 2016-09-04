package no.javazone.fedex4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.stream.IntStream.*;
import static java.util.stream.Collectors.*;

public class FedEx4J {


    /**
     * The courier organizes packages in boxes for each customer
     * Each box has the recipient and a collection of packages to deliver
     */
    static class CourierBox {
        final Customer customer;
        final Queue<Package> mailBox = new ConcurrentLinkedQueue<>();

        CourierBox(Customer customer) {
            this.customer = customer;
        }

    }

    static class Courier implements Runnable {

        List<CourierBox> customerPackages = new CopyOnWriteArrayList<>();
        Thread myThread;

        @Override
        public void run() {
            myThread = Thread.currentThread();
            while (!Thread.currentThread().isInterrupted()) {
                customerPackages.stream()
                        .forEach(box -> {
                            Package pkg = box.mailBox.poll();
                            if (pkg != null) {
                                box.customer.onMessage(pkg);
                            }
                        });
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

    int processorCount = Runtime.getRuntime().availableProcessors();
    final Map<Customer, CourierBox> boxForCustomer = new ConcurrentHashMap<>();
    final List<Courier> couriers = new CopyOnWriteArrayList<>();

    final Map<Class, List<CourierBox>> customersByType = new ConcurrentHashMap<>();

    public FedEx4J() {
        couriers.addAll(range(0, processorCount)
                .mapToObj(i -> new Courier().startShift())
                .collect(toList()));
    }


    public List<Customer> add1CustomerPerCourier(Supplier<Customer> customer) {
        return couriers.stream()
                .map(c -> addCustomerToCourier(customer.get(), c))
                .collect(toList());
    }

    public Customer addCustomer(Customer customer) {

        //assign to courier
        int randomCourier = ThreadLocalRandom.current().nextInt(processorCount);
        Courier courier = couriers.get(randomCourier);
        return addCustomerToCourier(customer, courier);

    }

    private Customer addCustomerToCourier(Customer customer, Courier courier) {
        if (boxForCustomer.containsKey(customer)) return customer;

        //set up a box for packages destined for this customer
        CourierBox box = new CourierBox(customer);
        boxForCustomer.put(customer, box);

        courier.customerPackages.add(box);

        customersByType.putIfAbsent(customer.getClass(), new CopyOnWriteArrayList<>());
        customersByType.get(customer.getClass()).add(box);

        return customer;
    }


    public Customer findCustomerOfType(Class<? extends Customer> clazz) {
        List<CourierBox> courierBoxes = customersByType.get(clazz);
        int randomSelection = ThreadLocalRandom.current().nextInt(courierBoxes.size());
        return courierBoxes.get(randomSelection).customer;
    }

    public void sendPackage(Package pkg) {
        boxForCustomer.get(pkg.to).mailBox.offer(pkg);
    }

    public void close() {
        couriers.forEach(c -> c.endShift());
    }

}
