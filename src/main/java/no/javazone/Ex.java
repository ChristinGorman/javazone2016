package no.javazone;

import sun.misc.Unsafe;

import java.util.function.Function;

@FunctionalInterface
public interface Ex<OUT, IN> {

    OUT call(IN params) throws InterruptedException;

    static <OUT, IN> Function<IN, OUT> wrap(Ex<OUT, IN> func) {
        return (param) -> {
            try {
                return func.call(param);
            }catch(Exception e) {
                Unsafe.getUnsafe().throwException(e);
                return null;
            }
        };
    }
}
