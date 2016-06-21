package no.javazone;

public class Threads {

    /**
     * Surprisingly good performance, but obviously only works up to a couple of thousand threads
     */
    public static void main(String[] args) throws Exception {
        new TaskRunner(10_000, Big::task).runOnThread();
    }

}
