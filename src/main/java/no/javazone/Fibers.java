package no.javazone;

import co.paralleluniverse.fibers.Fiber;

import java.util.stream.IntStream;

import static no.javazone.Big.numRuns;

public class Fibers {

    public static void main(String[] args) throws Exception{
        Metrics printer = new Metrics();
        IntStream.range(0,numRuns).forEach(i->new Fiber(() -> printer.track(Big::task)).start());
        printer.print();
    }

}
