package no.javazone.sleep;

public class NonBlockingSleeper {

    public static Long sleep1Sec(){
        return sleep(1000);
    }

    public static Long sleep(long millis) {
        long timeout = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < timeout) {
            Thread.yield();
        }
        return millis;
    }
}
