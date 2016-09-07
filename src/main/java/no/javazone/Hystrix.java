package no.javazone;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Scheduler;
import rx.internal.schedulers.ExecutorScheduler;

import java.util.concurrent.Executors;

public class Hystrix {

    static class BigTaskCommand extends HystrixCommand<Long> {

        BigTaskCommand() {
            super(HystrixCommandGroupKey.Factory.asKey("BigTaskGroup"));
        }

        @Override
        protected Long run() throws Exception {
            print("Calculating big number!");
            return Big.task();
        }
    }

    public static void main(String[] args) {
        print("Starting!");

        print("Big number: " + new BigTaskCommand().execute());

        //Observable<Long> numObservable = new BigTaskCommand().observe();
            //.observeOn(new ExecutorScheduler(Executors.newSingleThreadExecutor()));

        //numObservable.subscribe(bigNum -> print("Observed number: " + bigNum));

        print("Finished");
    }

    private static void print(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
