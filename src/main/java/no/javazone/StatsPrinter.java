package no.javazone;

import java.text.NumberFormat;

public class StatsPrinter {

    public static String stats(long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(true);

        return String.format("duration: %s, memory: %s", format.format(duration), format.format(Runtime.getRuntime().totalMemory()));
    }
}
