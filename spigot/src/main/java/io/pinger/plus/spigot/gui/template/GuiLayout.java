package io.pnger.gui.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class GuiLayout implements ConfigurationSerializable {
    private final String[] layout;
    private final int rows;

    public GuiLayout(Builder builder) {
        this.layout = builder.layout;
        this.rows = builder.rows;
    }

    public GuiLayout(Map<String, Object> map) {
        this(Builder.from(map));
    }

    public static Builder builder(int rows) {
        return new Builder(rows);
    }

    public String[] getLayout() {
        return this.layout;
    }

    public int getRows() {
        return this.rows;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("rows", this.rows);
        map.put("layout", Arrays.asList(this.layout));
        return map;
    }

    public static class Builder {
        private final int rows;

        private String[] layout;

        public Builder(int rows) {
            this.rows = rows;
            this.layout = new String[rows];
            Arrays.fill(this.layout, "?????????");
        }

        @SuppressWarnings("unchecked")
        public static Builder from(Map<String, Object> map) {
            final int rows = (Integer) map.get("rows");
            final List<String> layout = (List<String>) map.get("layout");
            return new Builder(rows).layout(layout);
        }

        public Builder layout(String[] layout) {
            this.layout = layout;
            return this;
        }

        public Builder layout(List<String> layout) {
            this.layout = layout.toArray(new String[0]);
            return this;
        }

        public Builder row(int row, @Nonnull String layout) {
            this.layout[row] = layout;
            return this;
        }

        public Builder row(@Nonnull String pattern, int... rows) {
            for (final int row : rows) {
                this.row(row, pattern);
            }
            return this;
        }

        public GuiLayout build() {
            return new GuiLayout(this);
        }
    }
}
