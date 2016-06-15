package no.javazone.sleep;

import no.javazone.TaskRunner;

public class ThreadSleep {

    /**
    Works as long as the number of threads is kept below a few thousand
     */
    public static void main(String[] args) throws Exception {
        new TaskRunner(1000, Sleeper::sleep1Sec).runOnThread();
    }
}
