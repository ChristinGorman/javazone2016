package no.javazone.sleep;

import no.javazone.Big;
import no.javazone.Metrics;

import java.util.stream.IntStream;

public class ThreadSleep {

    public static void main(String[] args) throws Exception {
        Metrics printer =new Metrics();
        IntStream.range(0, Big.numRuns)
                .mapToObj(i -> printer.trackRunnable(BlockingSleeper::sleep1Sec))
                .forEach(i -> new Thread(i).start());
        printer.print();
    }
}
