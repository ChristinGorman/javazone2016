package no.javazone;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static no.javazone.util.Timer.time;

public class BigTest {

    private static final int COUNT = 10000;

    @Test
    public void testSingle() throws Exception {
        time(() -> {
            for (int i = 0; i < COUNT; i++) {
                Big.task();
            }
        });
    }

    @Test
    public void testExec() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Long>> futures = new ArrayList<>(COUNT);
        time(() -> {
            for (int i = 0; i < COUNT; i++) {
                futures.add(executor.submit(Big::task));
            }
            System.out.println("Started " + COUNT + " tasks");
            for (int i = 0; i < futures.size(); i++) {
                futures.get(i).get();
                if ((i + 1) % 1000 == 0) {
                    System.out.println("Collected " + (i + 1) + " tasks");
                }
            }
        });
    }
}
