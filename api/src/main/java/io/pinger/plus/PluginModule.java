package io.pinger.plus;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.pinger.plus.inject.Listener;
import io.pinger.plus.plugin.logging.PluginLogger;

public class PluginModule extends AbstractModule {
    private final AbstractBootstrap bootstrap;

    public PluginModule(AbstractBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void configure() {
        this.bind(AbstractBootstrap.class).toInstance(this.bootstrap);
        this.bind(PluginLogger.class).toInstance(this.bootstrap.getLogger());
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
