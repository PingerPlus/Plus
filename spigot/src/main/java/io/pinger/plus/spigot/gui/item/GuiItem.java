package io.pinger.plus.spigot.gui.item;

import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.bukkit.inventory.ItemStack;

public class GuiItem {
    private final ItemStack item;
    private final int slot;
    private final String state;
    private final GuiButtonTemplate template;

    private boolean dragging;

    private GuiItem(@Nonnull ItemStack item, int slot, @Nonnull String state, @Nonnull GuiButtonTemplate template) {
        this.item = item;
        this.slot = slot;
        this.state = state;
        this.template = template;
    }

    public static GuiItem of(@Nonnull ItemStack item, int slot, @Nonnull String state, @Nonnull GuiButtonTemplate template) {
        return new GuiItem(item, slot, state, template);
    }

    public int getSlot() {
        return this.slot;
    }

    public String getState() {
        return this.state;
    }

    public GuiButtonTemplate getTemplate() {
        return this.template;
    }

    /**
     * This method returns whether we can drag items from the bottom inventory
     * into this slot.
     * <p>
     * Usually, we never want to allow items from the bottom inventory to be dragged into slots
     * on the top inventory like this, but in special cases like in Trade System plugins, we need
     * an option to do this.
     * </p>
     *
     * @return whether we can drag an item from the bottom inventory to this slot in the top inventory
     */

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public ItemStack getItemStack() {
        return this.item;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final GuiItem other = (GuiItem) object;
        return this.slot == other.slot &&
               Objects.equals(this.item, other.item) &&
               Objects.equals(this.state, other.state) &&
               Objects.equals(this.template, other.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.item, this.slot, this.state, this.template);
    }

    @Override
    public String toString() {
        return "GuiItem{" +
               "item=" + this.item +
               ", slot=" + this.slot +
               ", state='" + this.state + '\'' +
               ", template=" + this.template +
               ", dragging=" + this.dragging +
               '}';
    }
}
