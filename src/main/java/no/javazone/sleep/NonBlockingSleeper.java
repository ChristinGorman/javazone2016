package no.javazone.sleep;

public class NonBlockingSleeper {
    public static void sleep(int millis) {
        long timeout = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < timeout) {
            Thread.yield();
        }
    }
}
