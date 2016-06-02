package no.javazone;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ThreadTest {

    @Test
    public void should_run_8_times_as_fast_with_8_cores() throws Exception {
        long t = System.currentTimeMillis();
        int processors = Runtime.getRuntime().availableProcessors();
        IntStream.range(0, processors * 100).forEach(i->Big.task());
        long duration = System.currentTimeMillis() - t;
        long mem = Runtime.getRuntime().totalMemory();
        TaskRunner runner = new TaskRunner(processors * 100);
        Supplier task = Big::task;
        TaskRunner.Result result = runner.runTask(() -> new Thread(runner.track(task)).start());

        Assert.assertTrue(String.format("Used %d compared to %d", result.memory, mem), result.memory > mem * processors/2);
        Assert.assertTrue(String.format("Expecting 8 times speedup, got only %d. Single Threaded time was %d", duration / result.duration, duration), result.duration <= (duration / (processors-1)));
    }
}
