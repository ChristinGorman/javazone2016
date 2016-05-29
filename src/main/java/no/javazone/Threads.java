package no.javazone;

import static no.javazone.RunConfig.numRuns;

public class Threads {

    public static void main(String[] args) throws Exception{
        Metrics metrics = new Metrics(numRuns);
        Runnable task = metrics.trackRunnable(Big::task);
        metrics.runTask(() -> new Thread(task).start());
    }

}
