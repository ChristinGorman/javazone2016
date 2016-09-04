package no.javazone.fedex4j;

public class Package<T> {
    public final T payload;
    public final Customer from;
    public final Customer<T> to;

    public Package(Customer<T> to, T payload, Customer from) {
        this.to = to;
        this.payload = payload;
        this.from = from;
    }
}
