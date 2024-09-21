package io.pnger.gui.template.button;

import io.pnger.gui.item.ItemBuilder;
import io.pnger.gui.item.ItemStackSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonState implements ConfigurationSerializable {
    private String name;
    private ItemStack item;

    public ButtonState(String name, ItemStack item) {
        this.name = name;
        this.item = item;
    }

    public ButtonState(Builder builder) {
        this.name = builder.name;
        this.item = builder.item;
    }

    public ButtonState(Map<String, Object> map) {
        this("", map);
    }

    public ButtonState(String name, Map<String, Object> map) {
        this(Builder.from(name, map));
    }

    public static Builder builder() {
        return new Builder();
    }

    public ButtonState copy() {
        return new ButtonState(this.name, this.item.clone());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setItem(UnaryOperator<ItemBuilder> modifier) {
        this.setItem(modifier.apply(ItemBuilder.create(this.item)).build());
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("item", ItemStackSerializer.serialize(this.item));
        return map;
    }

    public static class Builder {
        private String name;
        private ItemStack item;

        @SuppressWarnings("unchecked")
        public static Builder from(String name, Map<String, Object> map) {
            final ItemStack item = ItemStackSerializer.deserialize((Map<String, Object>) map.get("item"));
            return new Builder().name(name).item(item);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder item(ItemStack item) {
            this.item = item;
            return this;
        }

        public Builder item(UnaryOperator<ItemBuilder> modifier) {
            return this.item(modifier.apply(ItemBuilder.create(this.item)).build());
        }

        public ButtonState build() {
            return new ButtonState(this.name, this.item);
        }
    }
}
