package io.pinger.plus.text;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.text.replacers.CollectionReplacer;
import io.pinger.plus.text.replacers.ComponentReplacer;
import io.pinger.plus.text.replacers.ReplacerProvider;
import io.pinger.plus.text.replacers.StringReplacer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class Replacers {
    private final Set<ReplacerProvider<?>> providers = new HashSet<>();

    public Replacers() {
        this.registerProviders(
            new StringReplacer(),
            new CollectionReplacer(),
            new ComponentReplacer()
        );

        Instances.register(this);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> T accept(@NotNull T object, @NotNull Replacer replacer) {
        for (final ReplacerProvider provider : this.providers) {
            if (!provider.provides(object.getClass())) {
                continue;
            }

            return (T) provider.provide(object, replacer);
        }
        return object;
    }

    public ReplacerProvider<?> findProvider(Class<?> clazz) {
        for (final ReplacerProvider<?> provider : this.providers) {
            if (provider.provides(clazz)) {
                return provider;
            }
        }
        return null;
    }

    private void registerProviders(ReplacerProvider<?>... providers) {
        this.providers.addAll(Arrays.asList(providers));
    }

}
