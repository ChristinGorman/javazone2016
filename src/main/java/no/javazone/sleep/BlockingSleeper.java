package no.javazone.sleep;

public class BlockingSleeper {
    public static Long sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return millis;
    }

    public static Long sleep1Sec() {
        return sleep(1000);
    }
}
