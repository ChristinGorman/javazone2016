package no.javazone;

import static no.javazone.RunConfig.numRuns;

public class Threads {

    public static void main(String[] args) throws Exception{
        TaskRunner taskRunner = new TaskRunner(numRuns);
        Runnable task = taskRunner.trackRunnable(Big::task);
        taskRunner.runTask(() -> new Thread(task).start());
    }

}
