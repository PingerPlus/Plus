package io.pinger.plus.spigot.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This class is essentially a wrapper class for the {@link org.bukkit.event.inventory.InventoryClickEvent}
 * <p>
 * Due to how we process click consumers, we use this class to minimize any casting needed with
 * the {@link InventoryClickEvent#getWhoClicked()}, and use more functional code.
 * </p>
 */

public class ClickEvent extends InventoryClickEvent {

    public ClickEvent(InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction());
    }

    public Player getPlayer() {
        return (Player) this.getWhoClicked();
    }

}
