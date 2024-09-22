package io.pinger.plus.spigot.gui.contents;

import io.pinger.plus.spigot.gui.event.ClickEvent;
import io.pinger.plus.spigot.gui.item.GuiItem;
import io.pinger.plus.spigot.gui.pagination.GuiPagination;
import io.pinger.plus.spigot.gui.template.GuiTemplate;
import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;
import io.pinger.plus.spigot.item.ItemBuilder;
import io.pinger.plus.util.Processor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.inventory.ItemStack;

public interface GuiContents {

    /**
     * Fills the inventory with items based on the associated {@link GuiTemplate}.
     * This method should be called to initialize the GUI contents when the inventory is opened.
     */
    void fillInventory();

    /**
     * Returns the {@link GuiTemplate} associated with this {@link GuiContents}.
     * <p>
     * The {@link GuiTemplate} contains the layout, button configuration, and general design
     * settings for the GUI, defining how the GUI should be structured and which elements it includes.
     * </p>
     *
     * @return The {@link GuiTemplate} that defines the structure and elements of the GUI.
     */
    GuiTemplate getTemplate();

    /**
     * Finds and returns the {@link GuiButtonTemplate} associated with the specified symbol
     * in the current {@link GuiTemplate}. The symbol is used to identify a specific button
     * configuration within the template.
     *
     * @param symbol The character symbol representing the button to find.
     * @return The {@link GuiButtonTemplate} associated with the specified symbol,
     *         or {@code null} if no button template is found.
     */
    default GuiButtonTemplate findButtonTemplate(char symbol) {
        return this.getTemplate().findButtonTemplate(symbol);
    }

    /**
     * Finds and returns the {@link GuiButtonTemplate} associated with the specified identifier
     * in the current {@link GuiTemplate}. The identifier is used to identify a specific button
     * configuration within the template.
     *
     * @param identifier The string identifier representing the button to find.
     * @return The {@link GuiButtonTemplate} associated with the specified identifier,
     *         or {@code null} if no button template is found.
     */
    default GuiButtonTemplate findButtonTemplate(String identifier) {
        return this.getTemplate().findButtonTemplate(identifier);
    }

    /**
     * Remaps an item and optionally caches the remapping for a specified duration.
     * <p>
     * If caching is enabled (`cacheFor` > 0), the item will be remapped and the result cached
     * for the specified duration in milliseconds. If `cacheFor` is 0, the remapping is applied every tick.
     * If `cacheFor` is negative, the item will be remapped once without caching.
     * </p>
     *
     * @param identifier A unique identifier for the item to be remapped.
     * @param modifier   The {@link Processor} that modifies the {@link ItemBuilder}.
     * @param cacheFor   The duration in milliseconds to cache the remapping. A value of 0 updates every tick,
     *                   a positive value caches the result for the given duration, and a negative value remaps without caching.
     */
    void remapItems(String identifier, Processor<ItemBuilder> modifier, long cacheFor);

    /**
     * Remaps an item without caching.
     * <p>
     * This method applies the remapping operation to the item once without any caching,
     * effectively calling {@link #remapItems(String, Processor, long)} with a cache duration of -1.
     * </p>
     *
     * @param identifier A unique identifier for the item to be remapped.
     * @param modifier   The {@link Processor} that modifies the {@link ItemBuilder}.
     */
    default void remapItems(String identifier, Processor<ItemBuilder> modifier) {
        this.remapItems(identifier, modifier, -1);
    }

    /**
     * Remaps an item and updates it every tick.
     * <p>
     * This method applies the remapping operation to the item every tick, effectively calling
     * {@link #remapItems(String, Processor, long)} with a cache duration of 0.
     * </p>
     *
     * @param identifier A unique identifier for the item to be remapped.
     * @param modifier   The {@link Processor} that modifies the {@link ItemBuilder}.
     */
    default void remapItemsAndTick(String identifier, Processor<ItemBuilder> modifier) {
        this.remapItems(identifier, modifier, 0);
    }

    /**
     * Reloads and updates any remapped items based on their identifiers and caching settings.
     * This method should be called to ensure that items are current and reflect any changes made.
     */
    void reloadRemappedItems();

    /**
     * Returns the pagination object associated with this GUI.
     *
     * @param <T> The type of the items in the pagination.
     * @return The {@link GuiPagination} associated with this GUI.
     */
    <T> GuiPagination<T> getPagination();

    /**
     * Creates and sets the pagination object for this GUI.
     *
     * @param pagination The {@link GuiPagination} to be set.
     * @param <T>       The type of the items in the pagination.
     */
    <T> void createPagination(GuiPagination<T> pagination);

    /**
     * Returns all items assigned to the current inventory, limited to the items present on the current page.
     * <p>
     * This method will not return items that are not displayed on the current page.
     * </p>
     *
     * @return A collection of {@link GuiItem} instances limited to the current page.
     */
    Collection<GuiItem> getItems();

    /**
     * Adds a {@link GuiItem} to the specified slot in the inventory.
     * <p>
     * Caution should be exercised when using this method after the inventory has been built,
     * as it may alter the data associated with the {@link GuiItem}.
     * </p>
     *
     * @param slot the slot to place the item in
     * @param item the item to be placed in this slot
     */
    void setItem(int slot, GuiItem item);

    /**
     * Returns a list of items associated with the specified identifier.
     *
     * @param identifier The unique identifier for the items to retrieve.
     * @return A list of {@link GuiItem} instances associated with the specified identifier.
     */
    List<GuiItem> getItems(String identifier);

    /**
     * Returns a list of item slots associated with the specified identifier.
     *
     * @param identifier The unique identifier for the items to retrieve slots for.
     * @return A list of slot indices where the items are located.
     */
    default List<Integer> getItemSlots(String identifier) {
        return this.getItems(identifier).stream().map(GuiItem::getSlot).collect(Collectors.toList());
    }

    /**
     * Adds a click handler for the specified item identifier.
     * <p>
     * The click handler will be called when the item is clicked by the player.
     * </p>
     *
     * @param identifier A unique identifier for the item to associate the click handler with.
     * @param handler    The {@link Consumer} to handle the click event.
     */
    void addClickHandler(String identifier, Consumer<ClickEvent> handler);

    /**
     * Handles a click event for the specified item identifier.
     * <p>
     * This method invokes the associated click handler, if one exists, for the given identifier.
     * </p>
     *
     * @param identifier A unique identifier for the item that was clicked.
     * @param event      The {@link ClickEvent} that occurred.
     */
    void handleClick(String identifier, ClickEvent event);

    /**
     * Retrieves the {@link GuiItem} located at the specified slot.
     *
     * @param slot The slot index to retrieve the item from.
     * @return An {@link Optional} containing the {@link GuiItem}, or {@code empty} if no item exists at that slot.
     */
    Optional<GuiItem> getItem(int slot);

    /**
     * Retrieves the {@link GuiItem} located at the specified row and column.
     *
     * @param row The row index.
     * @param col The column index.
     * @return An {@link Optional} containing the {@link GuiItem}, or {@code empty} if no item exists at that location.
     */
    default Optional<GuiItem> getItem(int row, int col) {
        return this.getItem(row * 9 + col);
    }

    /**
     * Retrieves the {@link ItemStack} located at the specified slot.
     *
     * @param slot The slot index to retrieve the item stack from.
     * @return An {@link Optional} containing the {@link ItemStack}, or {@code empty} if no item stack exists at that slot.
     */
    Optional<ItemStack> getItemStack(int slot);

    /**
     * Retrieves the {@link ItemStack} located at the specified row and column.
     *
     * @param row The row index.
     * @param col The column index.
     * @return An {@link Optional} containing the {@link ItemStack}, or {@code empty} if no item stack exists at that location.
     */
    default Optional<ItemStack> getItemStack(int row, int col) {
        return this.getItemStack(row * 9 + col);
    }

    /**
     * Sets a {@link GuiItem} at the specified row and column.
     *
     * @param row  The row index to set the item in.
     * @param col  The column index to set the item in.
     * @param item The {@link GuiItem} to set.
     */
    default void setItem(int row, int col, GuiItem item) {
        this.setItem(row * 9 + col, item);
    }

}
