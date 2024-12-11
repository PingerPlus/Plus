package io.pinger.plus.runtime;

public interface ClassInitializer {

    boolean accepts(Class<?> clazz);

    void initialize(Class<?> clazz);

}
