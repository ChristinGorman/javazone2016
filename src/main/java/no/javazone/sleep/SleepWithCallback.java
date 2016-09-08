package no.javazone.sleep;

import no.javazone.TaskRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepWithCallback {

    /**
    NOW we can execute as many tasks as we want with normal threads.
     */
    public static void main(String[] args) throws Exception {

        TaskRunner taskRunner = new TaskRunner(1_000_000);
        taskRunner.runTask(() -> Sleeper.sleep(1000, () -> taskRunner.countDown()));

    }
}
