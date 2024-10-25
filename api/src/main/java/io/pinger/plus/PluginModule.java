package io.pinger.plus;

import static io.pinger.plus.inject.ClassMatcher.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.pinger.plus.annotation.AutoBind;
import io.pinger.plus.inject.ClassListener;
import io.pinger.plus.inject.Listener;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.subscribe.Subscribable;
import java.lang.reflect.Method;

public class PluginModule extends AbstractModule {
    private final Bootstrap bootstrap;

    public PluginModule(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void configure() {
        this.bind(Bootstrap.class).toInstance(this.bootstrap);
        this.bind(PluginLogger.class).toInstance(this.bootstrap.getLogger());
        this.handleBindableClasses();
    }

    private void handleBindableClasses() {
        this.bindListenerToAny(annotation(AutoBind.class), (ClassListener) (instance, classifier) -> {
            final Method[] methods = classifier.getDeclaredMethods();
            for (final Method method : methods) {
                final int params = method.getParameterCount();
                final Class<?> returnType = method.getReturnType();
                if (params != 0) {
                    continue; // Skip method with != 0 params
                }

                if (!Subscribable.class.isAssignableFrom(returnType)) {
                    continue;
                }

                method.setAccessible(true);
                method.invoke(instance);
            }
        });
    }

    protected void bindListenerToAny(Matcher<? super TypeLiteral<?>> matcher, Listener<Object> listener) {
        this.bindListener(matcher, Object.class, listener);
    }

    @SuppressWarnings("unchecked")
    protected <T> void bindListener(Matcher<? super TypeLiteral<?>> matcher, Class<T> classifier, Listener<? super T> listener) {
        this.bindListener(matcher, new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                if (!classifier.isAssignableFrom(type.getRawType())) {
                    return;
                }

                encounter.register((Listener<? super I>) listener);
            }
        });
    }
}
