package no.javazone;

import static no.javazone.Big.numRuns;

public class Sequence {

    public static void main(String[] args) throws Exception{
        Metrics printer = new Metrics();

        for (int i = 0; i < numRuns; i++) {
            printer.track(Big::task).call();
        }

        printer.print();
    }

}
