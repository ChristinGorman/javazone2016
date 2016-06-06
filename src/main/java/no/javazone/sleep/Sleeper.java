package no.javazone.sleep;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sleeper {

    static class SleepCallback {
        final long done;
        final Runnable callback;
        public SleepCallback(long done, Runnable r) {
            this.done  = done;
            this.callback = r;
        }
    }

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

    static Queue<SleepCallback> sleepTasks = new LinkedBlockingQueue<>();
    static {
        new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                SleepCallback first = sleepTasks.poll();
                if (first != null) {
                    if (System.currentTimeMillis() >= first.done) {
                        first.callback.run();
                    } else {
                        sleepTasks.offer(first);
                    }
                }
            }
        }).start();
    }

    public static void sleep(int millis, Runnable callback) {
        sleepTasks.offer(new SleepCallback(System.currentTimeMillis() + millis, callback));
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
