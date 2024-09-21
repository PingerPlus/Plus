package io.pnger.gui.template;

import com.google.common.base.Preconditions;
import io.pnger.gui.template.button.GuiButtonTemplate;
import io.pnger.gui.util.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class GuiTemplate implements ConfigurationSerializable {
    private final List<GuiButtonTemplate> buttons;
    private final String title;
    private final GuiLayout layout;

    public GuiTemplate(Builder builder) {
        this.buttons = builder.buttons;
        this.title = builder.title;
        this.layout = builder.layout;
    }

    public GuiTemplate(Map<String, Object> map) {
        this(Builder.from(map));
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<GuiButtonTemplate> getButtons() {
        return this.buttons;
    }

    public GuiButtonTemplate findButtonTemplate(char symbol) {
        return this.getButton(GuiButtonTemplate::getSymbol, symbol);
    }

    public GuiButtonTemplate findButtonTemplate(String identifier) {
        return this.getButton(GuiButtonTemplate::getIdentifier, identifier);
    }

    public <T> GuiButtonTemplate getButton(Function<GuiButtonTemplate, T> function, T param) {
        return Iterables.query(this.buttons, function, param).stream().findAny().orElse(null);
    }

    public String getTitle() {
        return this.title;
    }

    public GuiLayout getLayout() {
        return this.layout;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("title", this.title);
        map.put("layout", this.layout.serialize());
        map.put("buttons", this.buttons.stream().map(GuiButtonTemplate::serialize).toList());
        return map;
    }

    public static class Builder {
        private final List<GuiButtonTemplate> buttons;

        private String title;
        private GuiLayout layout;

        public Builder() {
            this.buttons = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        public static Builder from(Map<String, Object> map) {
            final String title = (String) map.get("title");
            final GuiLayout layout = new GuiLayout((Map<String, Object>) map.get("layout"));
            final List<GuiButtonTemplate> buttons = new ArrayList<>();
            final List<Map<String, Object>> buttonsSerialized = (List<Map<String, Object>>) map.get("buttons");
            for (final Map<String, Object> buttonMap : buttonsSerialized) {
                buttons.add(new GuiButtonTemplate(buttonMap));
            }
            return new Builder().title(title).layout(layout).buttons(buttons);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder design(int rows, @Nonnull UnaryOperator<GuiLayout.Builder> modifier) {
            Preconditions.checkArgument(rows <= 6, "Row amount must be less or equal than 6");
            this.layout = modifier.apply(GuiLayout.builder(rows)).build();
            return this;
        }

        private Builder layout(GuiLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder button(@Nonnull UnaryOperator<GuiButtonTemplate.Builder> modifier) {
            this.buttons.add(modifier.apply(GuiButtonTemplate.builder()).build());
            return this;
        }

        private Builder buttons(List<GuiButtonTemplate> buttons) {
            this.buttons.addAll(buttons);
            return this;
        }

        public GuiTemplate build() {
            return new GuiTemplate(this);
        }
    }
}