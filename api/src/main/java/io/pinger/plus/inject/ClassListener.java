package io.pinger.plus.inject;

public interface ClassListener extends Listener<Object> {

    void afterInject(Object instance, Class<?> classifier) throws Exception;

    @Override
    default void afterInject(Object instance) throws Exception {
        this.afterInject(instance, instance.getClass());
    }
}
