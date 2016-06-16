package no.javazone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MemoryFail {

    public static void main(String[] args) throws Exception {

        Test test = new Test();
        new TaskRunner(10_000, ()-> test.updateMe("rubbish")).runOnExecutor(Executors.newCachedThreadPool());
        System.out.println(test.number);
        System.out.println(test.list.size());
    }

    private static class Test {
        List<String> list = new ArrayList<>();
        long number = 0;

        public long updateMe(String whatever) {
            list.add(whatever);
            return number++;
        }
    }

}
