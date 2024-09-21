package io.pinger.plus.text;

import io.pinger.plus.message.ComponentSerializer;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ReplacerEntry {
    private final String key;
    private final Supplier<Component> content;

    protected ReplacerEntry(@NotNull Builder builder) {
        this.key = builder.key;
        this.content = builder.content;
    }

    public Component supplyContent() {
        return this.content.get();
    }

    public String key() {
        return this.key;
    }

    protected static class Builder {
        private String key;
        private Supplier<Component> content;

        protected Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder contentPlain(@NotNull String content) {
            return this.contentPlain(() -> Collections.singletonList(content));
        }

        public Builder contentPlain(@NotNull Supplier<List<String>> supplier) {
            this.content = () -> ComponentSerializer.linesToComponent(supplier.get());
            return this;
        }

        public Builder contentComponent(@NotNull Supplier<Component> supplier) {
            this.content = supplier;
            return this;
        }
    }

}
