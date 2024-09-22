package io.pinger.plus.spigot.gui.provider;

import io.pinger.plus.spigot.gui.contents.GuiContents;
import org.bukkit.entity.Player;

/**
 * Represents a contract for creating and managing custom GUI's.
 * <p>
 * This interface defines methods for initializing and updating the GUI:
 * <ul>
 *     <li>{@link #initialize(Player, GuiContents)} - Called once before the GUI is first displayed to set up initial items that do not require frequent updates.</li>
 *     <li>{@link #update(Player, GuiContents)} - Called periodically to refresh or update the GUI's dynamic content, such as player-dependent or interactive elements.</li>
 * </ul>
 * Implementations of this interface should provide logic for both the static setup of the GUI (via {@code initialize})
 * and the periodic refreshing of the GUI (via {@code update}) as needed.
 */
public interface GuiProvider {

    /**
     * Initializes the GUI for the specified player before it is loaded.
     * <p>
     * This method is called once to set up the GUI's initial state, including items that are static or do not require
     * frequent updates. It should be used to populate the GUI with items that remain constant.
     * </p>
     *
     * @param player   The {@link Player} for whom the GUI is being initialized. This player will be the one interacting with the GUI.
     * @param contents The {@link GuiContents} object representing the GUI's contents. Implementers should use this to define
     *                 the initial set of items and elements that will appear in the GUI.
     */
    void initialize(Player player, GuiContents contents);

    /**
     * Updates the GUI for the specified player at regular intervals.
     * <p>
     * This method is called periodically, typically every 1 tick, to refresh or update the GUI's dynamic content.
     * It should be used to handle elements that change frequently, such as progress indicators, cooldown timers,
     * real-time statistics, or any interactive elements that respond to player actions.
     * </p>
     *
     * @param player   The {@link Player} currently viewing the GUI.
     * @param contents The {@link GuiContents} object representing the current state of the GUI. This can be manipulated
     *                 to apply any changes needed during the update cycle.
     */
    default void update(Player player, GuiContents contents) {
        contents.reloadRemappedItems();
    }

}