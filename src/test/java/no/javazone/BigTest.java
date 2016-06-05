package no.javazone;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BigTest {

    private static final int COUNT = 10000;

    @Test
    public void testSingle() {
        long start = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            Big.task();
        }
        System.out.println("Result: " + COUNT + " in " + ((System.nanoTime() - start) / 1e9) + "s");
    }

    @Test
    public void testExec() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Long>> futures = new ArrayList<>(COUNT);
        long start = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            futures.add(executor.submit(Big::task));
        }
        for (Future<Long> future : futures) {
            future.get();
        }
        System.out.println("Result: " + COUNT + " in " + ((System.nanoTime() - start) / 1e9) + "s");
    }
}
