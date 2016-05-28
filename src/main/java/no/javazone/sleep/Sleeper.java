package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;

public class Sleeper {
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

    public static Long sleepwalk1Sec(){
        return sleepwalk(1000);
    }

    public static Long sleepwalk(long millis) {
        long timeout = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < timeout) {
            Thread.yield();
        }
        return millis;
    }

    @Suspendable
    public static Long fiberSleep() {
        try {
            Fiber.sleep(1000);
            return 1000l;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
