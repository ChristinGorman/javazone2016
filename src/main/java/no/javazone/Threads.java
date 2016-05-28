package no.javazone;

import static no.javazone.RunConfig.numRuns;

public class Threads {

    public static void main(String[] args) throws Exception{
        Metrics metrics = new Metrics(numRuns);
        metrics.runTask(() -> new Thread(metrics.trackRunnable(Big::task)).start());
    }

}
