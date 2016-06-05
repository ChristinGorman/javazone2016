package no.javazone.util;

public class Timer {

    public interface Timed {
        void apply() throws Exception;
    }

    public static void time(Timed timed) throws Exception {
        long start = System.nanoTime();
        timed.apply();
        System.out.println("Result in " + ((System.nanoTime() - start) / 1e9) + "s");
    }
}
