package no.javazone;

public class Sequence {

    public static void main(String[] args) throws Exception{
        Metrics m = new Metrics(RunConfig.numRuns);
        m.runTask(m.trackRunnable(Big::task));
    }

}
