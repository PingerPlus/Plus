package io.pinger.plus.spigot.gui.listener;

import com.google.inject.Inject;
import io.pinger.plus.annotation.AutoBind;
import io.pinger.plus.event.Events;
import io.pinger.plus.event.Priority;
import io.pinger.plus.instance.Instances;
import io.pinger.plus.spigot.gui.GuiInventory;
import io.pinger.plus.spigot.gui.GuiManager;
import io.pinger.plus.spigot.gui.event.ClickEvent;
import io.pinger.plus.spigot.gui.item.GuiItem;
import io.pinger.plus.subscribe.Subscribable;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

@AutoBind
public class GuiListener {
    private final GuiManager manager;

    @Inject
    public GuiListener(GuiManager manager) {
        this.manager = manager;
    }

    public Subscribable onInventoryClose() {
        return Events.listen(InventoryCloseEvent.class, Priority.LOW, (event) -> {
            final Player player = (Player) event.getPlayer();
            final GuiInventory inventory = this.manager.getInventory(player);
            if (inventory == null) {
                return;
            }

            inventory.getInventory().clear();
            this.manager.unregisterInventory(player);
        });
    }

    public Subscribable onPlayerQuit() {
        return Events.listen(PlayerQuitEvent.class, Priority.LOW, (event) -> {
            this.manager.unregisterInventory(event.getPlayer());
        });
    }

    public Subscribable onInventoryDrag() {
        return Events.listen(InventoryDragEvent.class, Priority.LOW, (event) -> {
            final Player player = (Player) event.getWhoClicked();
            final GuiInventory inventory = this.manager.getInventory(player);
            if (inventory == null) {
                return;
            }

            for (final int slot : event.getRawSlots()) {
                if (slot >= player.getOpenInventory().getTopInventory().getSize()) {
                    continue;
                }

                event.setCancelled(true);
                break;
            }
        });
    }

    public Subscribable onPluginDisable() {
        return Events.listen(PluginDisableEvent.class, Priority.LOW, (event) -> {
            final Plugin plugin = event.getPlugin();
            final Plugin currentPlugin = Instances.getOrThrow(Plugin.class);
            if (!plugin.equals(currentPlugin)) {
                return;
            }

            new HashMap<>(this.manager.getInventories()).forEach((id, inv) -> {
                final Player player = Bukkit.getPlayer(id);
                if (player == null) {
                    return;
                }

                inv.close(player);
            });
        });
    }

    public Subscribable onInventoryClick() {
        return Events.listen(InventoryClickEvent.class, (event) -> {
            final Player player = (Player) event.getWhoClicked();
            final InventoryAction action = event.getAction();
            final ClickType click = event.getClick();

            this.manager.onInventoryAction(player, (inv) -> {
                final Inventory clickedInventory = event.getClickedInventory();
                if (clickedInventory == player.getOpenInventory().getBottomInventory()) {
                    if (action == InventoryAction.COLLECT_TO_CURSOR || action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        event.setCancelled(true);
                        return;
                    }

                    if (action == InventoryAction.NOTHING && click != ClickType.MIDDLE) {
                        event.setCancelled(true);
                        return;
                    }
                    return;
                }

                // It's the top inventory
                final int row = event.getSlot() / 9;
                final int column = event.getSlot() % 9;
                if (row < 0 || column < 0) {
                    event.setCancelled(true);
                    return;
                }

                if (row >= inv.getRows()) {
                    event.setCancelled(true);
                    return;
                }

                final GuiItem item = inv.getContents().getItem(row, column).orElse(null);
                if (item == null) {
                    event.setCancelled(true);
                    return;
                }

                final String identifier = item.getTemplate().getIdentifier();
                final ClickEvent clickEvent = new ClickEvent(event);
                if (item.isDragging()) {
                    inv.getContents().handleClick(identifier, clickEvent);
                    return;
                }

                event.setCancelled(true);
                inv.getContents().handleClick(identifier, clickEvent);
            });
        });
    }
}
