package io.pnger.gui.template.button;

import io.pnger.gui.item.ItemBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiButtonTemplate implements ConfigurationSerializable {
    private final Map<String, ButtonState> states;
    private final char symbol;
    private final String identifier;

    private GuiButtonTemplate(Builder builder) {
        this.symbol = builder.symbol;
        this.identifier = builder.identifier;
        this.states = builder.states;
    }

    public GuiButtonTemplate(Map<String, Object> map) {
        this(Builder.from(map));
    }

    public static Builder builder() {
        return new Builder();
    }

    public char getSymbol() {
        return this.symbol;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Map<String, ButtonState> getStates() {
        return this.states;
    }

    public ButtonState getDefaultState() {
        return this.states.get("default");
    }

    public ButtonState getState(String name) {
        return this.states.get(name);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("symbol", this.symbol);
        map.put("identifier", this.identifier);

        final Map<String, Object> states = new HashMap<>();
        for (final Entry<String, ButtonState> entry : this.states.entrySet()) {
            states.put(entry.getKey(), entry.getValue().serialize());
        }

        map.put("states", states);
        return map;
    }

    public static class Builder {
        private final Map<String, ButtonState> states = new HashMap<>();

        private String identifier = null;
        private char symbol;

        Builder() {
        }

        public static Builder from(Map<String, Object> map) {
            final String identifier = (String) map.getOrDefault("identifier", null);
            final char symbol = (char) map.get("symbol");
            final Map<String, ButtonState> states = Builder.getStates(map);
            return new Builder().identifier(identifier).symbol(symbol).states(states);
        }

        @SuppressWarnings("unchecked")
        private static Map<String, ButtonState> getStates(Map<String, Object> map) {
            final Map<String, ButtonState> states = new HashMap<>();
            final Map<String, Object> serializedStates = (Map<String, Object>) map.get("states");
            for (final Entry<String, Object> entry : serializedStates.entrySet()) {
                final String stateName = entry.getKey();
                final Map<String, Object> stateMap = (Map<String, Object>) entry.getValue();
                states.put(stateName, new ButtonState(stateName, stateMap));
            }
            return states;
        }

        public Builder identifier(@Nullable String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder symbol(char symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder states(Map<String, ButtonState> states) {
            this.states.putAll(states);
            return this;
        }

        public Builder state(String name, UnaryOperator<ItemBuilder> builder) {
            return this.state(name, builder.apply(ItemBuilder.create()).build());
        }

        public Builder state(String name, ItemStack item) {
            this.states.put(name, ButtonState.builder().item(item).name(name).build());
            return this;
        }

        public Builder defaultState(UnaryOperator<ItemBuilder> builder) {
            return this.defaultState(builder.apply(ItemBuilder.create()).build());
        }

        public Builder defaultState(ItemStack item) {
            this.states.put("default", ButtonState.builder().item(item).name("default").build());
            return this;
        }

        public GuiButtonTemplate build() {
            return new GuiButtonTemplate(this);
        }
    }
}
