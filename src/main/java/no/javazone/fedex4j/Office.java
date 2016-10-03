package no.javazone.fedex4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Office<T> {

    private int count;
    final AtomicInteger customerIdx = new AtomicInteger(0);
    final List<Employee<T>> employees = new ArrayList<>();
    final ConcurrentMap<Employee, Boolean> available = new ConcurrentHashMap<>();

    public Office(int count, Supplier<Employee<T>> customer) {
        this.count = count;
        IntStream.range(0, count).forEach(i-> employees.add(customer.get()));
    }

    public Employee<T> availableRepresentative() {
        return getAttentionOf(employees.get(customerIdx.getAndIncrement() % count));
    }

    private Employee getAttentionOf(Employee employee) {
        Boolean gotAttention = available.put(employee, false);
        while (Boolean.FALSE.equals(gotAttention)) {
            gotAttention = available.put(employee, false);
        }
        return employee;
    }

    public boolean sayGoodbyeTo(Employee employee) {
        return available.put(employee, true);
    }

    public void onMessage(Package<T> message) {
        Employee<T> employee = availableRepresentative();
        employee.onMessage(message);
        sayGoodbyeTo(employee);
    }

}
