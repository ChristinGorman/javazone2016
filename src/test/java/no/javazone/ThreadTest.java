package no.javazone;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class ThreadTest {

    @Test
    public void should_run_8_times_as_fast_with_8_cores() throws Exception {
        int processors = Runtime.getRuntime().availableProcessors();
        int runs = processors * 100;
        TaskRunner.Result singleThreadedResult = singleThreaded(runs);
        TaskRunner.Result multiThreadedResult = multiThreaded(runs);
        String msg = String.format("Expecting 8 times speedup, got only %d. Single Threaded time was %d",
                singleThreadedResult.duration / multiThreadedResult.duration,
                singleThreadedResult.duration);

        Assert.assertTrue(
                msg,
                multiThreadedResult.duration <= (singleThreadedResult.duration / (processors-1)));
    }

    private TaskRunner.Result multiThreaded(int runs) throws Exception {
        TaskRunner runner = new TaskRunner(runs);
        return runner.runTask(() -> new Thread(runner.track(Big::task)).start());
    }

    private TaskRunner.Result singleThreaded(int runs) {
        long t = System.currentTimeMillis();
        IntStream.range(0, runs).forEach(i-> Big.task());
        return new TaskRunner.Result(System.currentTimeMillis() - t, Runtime.getRuntime().totalMemory());
    }
}
