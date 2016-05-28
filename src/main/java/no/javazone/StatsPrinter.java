package no.javazone;

import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

public class StatsPrinter {

    public static String stats(long startTime, long remaining) {
        long duration = System.currentTimeMillis() - startTime;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        return String.format("remaining: %s, duration: %s, memory: %s", format.format(remaining), format.format(duration), format.format(Runtime.getRuntime().totalMemory()));
    }
}
