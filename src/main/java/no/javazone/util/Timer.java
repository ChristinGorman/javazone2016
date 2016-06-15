package no.javazone.util;

public class Timer {

    public interface Timed {
        void apply() throws Exception;
    }

    public static void time(Timed timed) throws Exception {
        long start = System.nanoTime();
        timed.apply();
        System.out.printf("Running time: %.3fs\n", ((System.nanoTime() - start) / 1e9));
    }
}
