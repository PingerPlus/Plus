package io.pinger.plus.spigot.gui;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.scheduler.Schedulers;
import io.pinger.plus.spigot.gui.template.GuiLayout;
import io.pinger.plus.spigot.gui.template.GuiTemplate;
import io.pinger.plus.spigot.gui.template.button.ButtonState;
import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;
import io.pinger.plus.subscribe.AutoSubscribable;
import io.pinger.plus.subscribe.Subscribable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

public class GuiManager implements AutoSubscribable {
    private final Map<UUID, GuiInventory> inventories;

    public GuiManager() {
        this.inventories = Collections.synchronizedMap(new HashMap<>());

        Instances.register(this);

        ConfigurationSerialization.registerClass(GuiTemplate.class);
        ConfigurationSerialization.registerClass(GuiLayout.class);
        ConfigurationSerialization.registerClass(GuiButtonTemplate.class);
        ConfigurationSerialization.registerClass(ButtonState.class);

        this.autoSubscribe();
    }

    public static GuiManager get() {
        return Instances.getOrThrow(GuiManager.class);
    }

    public boolean hasInventory(UUID uuid) {
        return this.inventories.containsKey(uuid);
    }

    public boolean hasInventory(Player player) {
        return this.inventories.containsKey(player.getUniqueId());
    }

    public boolean hasInventory(Player player, GuiInventory inventory) {
        final GuiInventory currentInv = this.inventories.get(player.getUniqueId());
        if (currentInv == null) {
            return false;
        }
        return currentInv.equals(inventory);
    }

    public GuiInventory getInventory(Player player) {
        return this.inventories.get(player.getUniqueId());
    }

    public void onInventoryAction(Player player, Consumer<GuiInventory> consumer) {
        final GuiInventory inventory = this.inventories.get(player.getUniqueId());
        if (inventory == null) {
            return;
        }
        consumer.accept(inventory);
    }

    public void registerInventory(Player player, GuiInventory inventory) {
        this.inventories.put(player.getUniqueId(), inventory);
    }

    public void unregisterInventory(Player player) {
        this.inventories.remove(player.getUniqueId());
    }

    public Map<UUID, GuiInventory> getInventories() {
        return this.inventories;
    }

    private Subscribable onInventoryTick() {
        return Schedulers.sync().runRepeating(() -> {
            new HashMap<>(this.inventories).forEach((uuid, gui) -> {
                final Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    return;
                }

                try {
                    gui.getProvider().update(player, gui.getContents());
                } catch (Exception e) {
                    Instances.get(PluginLogger.class).error("Failed to update inventory for player {}, {}", uuid, e);
                }
            });
        }, 1L, 1L);
    }
}
