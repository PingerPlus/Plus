package io.pinger.plus.subscribe;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface AutoSubscribable {

    default void autoSubscribe() {
        try {
            final Set<Method> subscribers = this.findSubscribers();
            for (final Method method : subscribers) {
                method.setAccessible(true);
                method.invoke(this);
            }
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Failed to invoke subscriber ", e);
        }
    }

    default Set<Method> findSubscribers() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> Subscribable.class.isAssignableFrom(method.getReturnType()))
            .collect(Collectors.toSet());
    }

}
