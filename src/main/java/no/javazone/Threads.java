package no.javazone;

import no.javazone.util.Timer;

import static no.javazone.RunConfig.numRuns;

public class Threads {

    /**
     * Surprisingly good performance, but obviously only works up to a couple of thousand threads
     */
    public static void main(String[] args) throws Exception {
        Timer.time(() -> {
            TaskRunner taskRunner = new TaskRunner(numRuns);
            Runnable task = taskRunner.track(Big::task);
            taskRunner.runTask(() -> new Thread(task).start());
        });
    }

}
