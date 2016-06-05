package no.javazone;

import no.javazone.util.Timer;

public class Sequence {

    /**
     * Only twice as slow and uses far less memory
     */
    public static void main(String[] args) throws Exception {
        Timer.time(() -> {
            TaskRunner m = new TaskRunner(RunConfig.numRuns);
            m.runTask(m.track(Big::task));
        });
    }

}
