package no.javazone.parallel;

import no.javazone.sleep.Sleeper;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Tasks {

    public static Integer sleepRandInt() {
        Sleeper.sleep1Sec();
        return new Random().nextInt();
    }

    public static String sleepStr() {
        Sleeper.sleep1Sec();
        //throw new RuntimeException("ouch!");
        return "javaBin";
    }
}
