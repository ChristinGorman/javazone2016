package no.javazone;

public class Sequence {

    public static void main(String[] args) throws Exception{
        TaskRunner m = new TaskRunner(RunConfig.numRuns);
        m.runTask(m.trackRunnable(Big::task));
    }

}
