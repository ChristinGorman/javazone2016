package no.javazone;

import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class Threads {

    public static void main(String[] args) throws Exception{
        Metrics printer = new Metrics();
        IntStream.range(0,numRuns).forEach(i->new Thread(() -> printer.track(Big::task)).start());
        printer.print();
    }

}
