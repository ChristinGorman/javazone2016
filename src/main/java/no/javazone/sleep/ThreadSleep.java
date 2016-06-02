package no.javazone.sleep;

import no.javazone.TaskRunner;
import no.javazone.RunConfig;

public class ThreadSleep {

    /**
    Works as long as the number of threads is kept below a few thousand
     */
    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(RunConfig.numRuns);
        Runnable task = taskRunner.track(Sleeper::sleep1Sec);
        taskRunner.runTask(() -> new Thread(task).start());
    }
}
