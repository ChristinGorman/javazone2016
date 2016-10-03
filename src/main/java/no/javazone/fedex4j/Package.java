package no.javazone.fedex4j;

public class Package<T> {
    public final T payload;
    public final Office from;
    public final Office<T> to;

    public Package(Employee<T> to, T payload, Employee from) {
        this.to = new Office(1, ()->to);
        this.payload = payload;
        this.from = new Office(1, ()->from);
    }

    public Package(Office<T> to, T payload, Office from) {
        this.to = to;
        this.payload = payload;
        this.from = from;
    }
}
