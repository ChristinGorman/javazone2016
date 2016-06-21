package no.javazone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutorLargeThreadPool {

    /**
     * Larger thread pool does not help one bit
     */
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        new TaskRunner(10_000, Big::task).runOnExecutor(executor);
    }

}
