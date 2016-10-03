package no.javazone.fedex4j;

@FunctionalInterface
public interface Employee<T> {
    void onMessage(Package<T> msg);
}
