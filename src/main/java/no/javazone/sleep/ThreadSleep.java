package no.javazone.sleep;

import no.javazone.TaskRunner;
import no.javazone.RunConfig;

public class ThreadSleep {

    public static void main(String[] args) throws Exception {
        TaskRunner taskRunner = new TaskRunner(RunConfig.numRuns);
        Runnable task = taskRunner.trackRunnable(Sleeper::sleep1Sec);
        taskRunner.runTask(() -> new Thread(task).start());
    }
}
