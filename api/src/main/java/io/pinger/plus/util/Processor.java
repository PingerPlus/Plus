package io.pinger.plus.util;

import java.util.function.Function;

@FunctionalInterface
public interface Processor<T> extends Function<T, T> {

    static <T> Processor<T> identity() {
        return t -> t;
    }

}
