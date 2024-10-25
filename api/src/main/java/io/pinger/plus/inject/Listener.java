package io.pinger.plus.inject;

import com.google.inject.spi.InjectionListener;
import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;

public interface Listener<T> extends InjectionListener<T> {

    void afterInject(T instance) throws Exception;

    @Override
    default void afterInjection(T instance) {
        try {
            this.afterInject(instance);
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Failed to handle object after injection", e);
        }
    }
}
