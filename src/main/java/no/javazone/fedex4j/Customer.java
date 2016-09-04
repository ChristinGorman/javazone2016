package no.javazone.fedex4j;

@FunctionalInterface
public interface Customer<T> {
    void onMessage(Package<T> msg);
}
