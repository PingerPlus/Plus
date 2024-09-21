package io.pinger.plus.text;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.text.ReplacerEntry.Builder;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Replacer {
    private final Set<ReplacerEntry> entries;

    public Replacer() {
        this.entries = Collections.synchronizedSet(new LinkedHashSet<>());
    }

    public Replacer replaceLiteral(String key, String value) {
        return this.replace((builder) -> builder.key(key).contentPlain(value));
    }

    public Replacer replaceComponent(String key, Component value) {
        return this.replace((builder) -> builder.key(key).contentComponent(() -> value));
    }

    public <T> T accept(@NotNull T object) {
        return Instances.get(Replacers.class).accept(object, this);
    }

    private Replacer replace(UnaryOperator<Builder> modifier) {
        final ReplacerEntry entry = new ReplacerEntry(modifier.apply(new Builder()));
        this.entries.removeIf((oldEntry) -> oldEntry.key().equals(entry.key()));
        this.entries.add(entry);
        return this;
    }

    public Set<ReplacerEntry> entries() {
        return this.entries;
    }

}
