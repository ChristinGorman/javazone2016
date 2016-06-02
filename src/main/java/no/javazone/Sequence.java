package no.javazone;

public class Sequence {

    /**
     * Only twice as slow and uses far less memory
     */
    public static void main(String[] args) throws Exception{
        TaskRunner m = new TaskRunner(RunConfig.numRuns);
        m.runTask(m.track(Big::task));
    }

}
