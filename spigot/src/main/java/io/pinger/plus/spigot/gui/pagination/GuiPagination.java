package io.pinger.plus.spigot.gui.pagination;

import io.pinger.plus.spigot.gui.contents.GuiContents;
import java.util.List;

public interface GuiPagination<T> {

    /**
     * Adds the current pagination state to the inventory.
     * <p>
     * This method should be called to display the items for the current page in the GUI.
     * </p>
     *
     * @return The current instance of {@link GuiPagination} for method chaining.
     */
    GuiPagination<T> addToInventory();

    /**
     * Sets the items to be displayed in the pagination.
     * <p>
     * This method associates a list of items with a specified identifier and provides a way to
     * manage how those items are presented in the pagination.
     * </p>
     *
     * @param identifier A unique identifier for the items being set.
     * @param items      The list of items to be displayed in the pagination.
     * @param provider   The {@link PageItemProvider} responsible for item management and presentation.
     * @return The current instance of {@link GuiPagination} for method chaining.
     */
    GuiPagination<T> setItems(String identifier, List<T> items, PageItemProvider<T> provider);

    /**
     * Returns the {@link GuiContents} associated with this pagination.
     *
     * @return The {@link GuiContents} instance managing the GUI items.
     */
    GuiContents getContents();

    /**
     * Sets the current page to the specified page number.
     *
     * @param page The page number to set as the current page.
     * @return The current instance of {@link GuiPagination} for method chaining.
     */
    GuiPagination<T> setPage(int page);

    /**
     * Retrieves the current page number.
     *
     * @return The current page number.
     */
    int getPage();

    /**
     * Retrieves the total number of pages available.
     *
     * @return The total number of pages.
     */
    int getPageCount();

    /**
     * Checks if the current page is the first page.
     *
     * @return {@code true} if the current page is the first page, {@code false} otherwise.
     */
    boolean isFirst();

    /**
     * Checks if the current page is the last page.
     *
     * @return {@code true} if the current page is the last page, {@code false} otherwise.
     */
    boolean isLast();

    /**
     * Navigates to the next page.
     *
     * @return The updated instance of {@link GuiPagination} representing the next page.
     */
    GuiPagination<T> next();

    /**
     * Navigates to the previous page.
     *
     * @return The updated instance of {@link GuiPagination} representing the previous page.
     */
    GuiPagination<T> previous();

    /**
     * Navigates to the first page.
     *
     * @return The updated instance of {@link GuiPagination} representing the first page.
     */
    GuiPagination<T> first();

    /**
     * Navigates to the last page.
     *
     * @return The updated instance of {@link GuiPagination} representing the last page.
     */
    GuiPagination<T> last();

    /**
     * Retrieves the list of items that are currently displayed on the active page.
     *
     * @return A list of items for the current page.
     */
    List<T> getItemsInPage();

}

