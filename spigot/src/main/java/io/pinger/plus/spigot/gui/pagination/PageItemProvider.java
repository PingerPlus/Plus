package io.pinger.plus.spigot.gui.pagination;

import io.pinger.plus.spigot.gui.template.button.ButtonState;
import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;

/**
 * Interface for providing item states in a paginated GUI system.
 * <p>
 * Implementing classes should define how to create a {@link ButtonState} for a given item
 * based on the item's type, its slot in the pagination, and the associated button template.
 * </p>
 *
 * @param <T> The type of item being provided for the pagination.
 */
public interface PageItemProvider<T> {

    /**
     * Provides a {@link ButtonState} for the specified item.
     *
     * @param t        The item for which to provide the button state.
     * @param slot     The slot index of the item within the pagination.
     * @param template The {@link GuiButtonTemplate} associated with the item.
     * @return The {@link ButtonState} representing the visual and functional state of the item.
     */
    ButtonState provide(T t, int slot, GuiButtonTemplate template);

}
