package io.pinger.plus.spigot.gui;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.spigot.gui.contents.GuiContents;
import io.pinger.plus.spigot.gui.contents.GuiContentsImpl;
import io.pinger.plus.spigot.gui.item.GuiItem;
import io.pinger.plus.spigot.gui.provider.GuiProvider;
import io.pinger.plus.spigot.gui.template.GuiTemplate;
import io.pinger.plus.util.Processor;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiInventory {
    private final GuiTemplate template;
    private final GuiProvider provider;
    private final GuiInventory parent;
    private final Processor<String> titleModifier;

    private AtomicReference<GuiContents> contents;
    private Inventory inventory;

    public GuiInventory(Builder builder) {
        this.template = builder.template;
        this.provider = builder.provider;
        this.parent = builder.parent;
        this.titleModifier = builder.titleModifier;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void open(Player player) {
        this.open(player, 0);
    }

    public void open(Player player, int page) {
        this.close(player, false);

        final GuiContents contents = new GuiContentsImpl(this, player.getUniqueId());
        this.contents.set(contents);

        try {
            this.provider.initialize(player, contents);
            if (!this.contents.get().equals(contents)) {
                return;
            }

            contents.fillInventory();
            contents.getPagination().setPage(page);

            this.inventory = this.openMenu(player);
            GuiManager.get().registerInventory(player, this);
        } catch (Exception e) {
            Instances.getOrThrow(PluginLogger.class).error("Failed to open inventory {}", e);
        }
    }

    public void close(Player player) {
        this.close(player, true);
    }

    public void close(Player player, boolean force) {
        GuiManager.get().unregisterInventory(player);
        this.contents.set(null);

        // If force is not set, don't close the inventory
        if (!force) {
            return;
        }

        player.closeInventory();
    }

    private Inventory openMenu(Player player) {
        final Inventory menu = Bukkit.createInventory(
            player,
            this.getRows() * 9,
            this.titleModifier.apply(this.template.getTitle())
        );

        // Fill in the inventory first
        for (final GuiItem item : this.contents.get().getItems()) {
            menu.setItem(item.getSlot(), item.getItemStack());
        }

        player.openInventory(menu);
        return menu;
    }

    public int getRows() {
        return this.template.getLayout().getRows();
    }

    public GuiTemplate getTemplate() {
        return this.template;
    }

    public GuiProvider getProvider() {
        return this.provider;
    }

    public GuiInventory getParent() {
        return this.parent;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public GuiContents getContents() {
        return this.contents.get();
    }

    public static class Builder {
        private GuiTemplate template;
        private GuiProvider provider;
        private GuiInventory parent;
        private Processor<String> titleModifier;

        public Builder template(GuiTemplate template) {
            this.template = template;
            return this;
        }

        public Builder provider(GuiProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder parent(GuiInventory parent) {
            this.parent = parent;
            return this;
        }

        public Builder title(Processor<String> titleModifier) {
            this.titleModifier = titleModifier;
            return this;
        }

        public GuiInventory build() {
            return new GuiInventory(this);
        }
    }


}
