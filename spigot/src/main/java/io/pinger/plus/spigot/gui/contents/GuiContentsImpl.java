package io.pinger.plus.spigot.gui.contents;

import io.pinger.plus.spigot.gui.GuiInventory;
import io.pinger.plus.spigot.gui.GuiManager;
import io.pinger.plus.spigot.gui.event.ClickEvent;
import io.pinger.plus.spigot.gui.item.CachedGuiItem;
import io.pinger.plus.spigot.gui.item.GuiItem;
import io.pinger.plus.spigot.gui.pagination.GuiPagination;
import io.pinger.plus.spigot.gui.pagination.GuiPaginationImpl;
import io.pinger.plus.spigot.gui.template.GuiLayout;
import io.pinger.plus.spigot.gui.template.GuiTemplate;
import io.pinger.plus.spigot.gui.template.button.ButtonState;
import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;
import io.pinger.plus.spigot.item.ItemBuilder;
import io.pinger.plus.util.Iterables;
import io.pinger.plus.util.Processor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiContentsImpl implements GuiContents {
    private final UUID uuid; // UUID of the player opening this inventory
    private final GuiInventory inventory;
    private final Map<Integer, GuiItem> items;
    private final GuiTemplate template;
    private final Map<String, CachedGuiItem> processors;
    private final Map<String, Consumer<ClickEvent>> clickHandlers;

    private GuiPagination<?> pagination;

    public GuiContentsImpl(GuiInventory inventory, UUID uuid) {
        this.inventory = inventory;
        this.uuid = uuid;
        this.template = inventory.getTemplate();
        this.processors = new HashMap<>();
        this.items = new HashMap<>();
        this.clickHandlers = new HashMap<>();
        this.pagination = new GuiPaginationImpl<>(this);
    }

    @Override
    public void fillInventory() {
        final GuiLayout layouts = this.template.getLayout();
        for (int row = 0; row < layouts.getRows(); row++) {
            final String current = layouts.getLayout()[row];
            for (int col = 0; col < current.length(); col++) {
                final char symbol = current.charAt(col);
                final GuiButtonTemplate button = this.template.findButtonTemplate(symbol);
                if (button == null) {
                    continue; // It should be material air, if this happens
                }

                final int slot = row * 9 + col;
                final GuiItem item = this.createGuiItem(slot, button);
                this.setItem(slot, item);
            }
        }
    }

    private GuiItem createGuiItem(int slot, GuiButtonTemplate template) {
        final ButtonState defaultState = template.getDefaultState();
        if (defaultState == null) {
            return GuiItem.of(
                new ItemStack(Material.AIR),
                slot,
                "default",
                template
            );
        }

        final ItemStack itemStack = defaultState.getItem().clone();
        return GuiItem.of(
            this.buildGuiItem(template, itemStack),
            slot,
            "default",
            template
        );
    }

    private ItemStack buildGuiItem(GuiButtonTemplate template, ItemStack stack) {
        final String identifier = template.getIdentifier();

        ItemStack currentStack = stack;
        if (identifier != null) {
            final CachedGuiItem cachedGuiItem = this.processors.get(identifier);
            if (cachedGuiItem != null) {
                currentStack = cachedGuiItem.getProcessor().apply(currentStack);
            }
        }

        return ItemBuilder.create(currentStack).build();
    }

    public void reloadRemappedItems() {
        for (final Entry<String, CachedGuiItem> entry : this.processors.entrySet()) {
            final String identifier = entry.getKey();
            final CachedGuiItem cachedGuiItem = entry.getValue();
            if (!cachedGuiItem.isCacheEnabled()) {
                continue;
            }

            final long cacheFor = cachedGuiItem.getCacheFor();
            final long lastCached = cachedGuiItem.getLastCached();
            if (cacheFor != 0 && (System.currentTimeMillis() - lastCached) < cacheFor) {
                return;
            }

            cachedGuiItem.setLastCached(System.currentTimeMillis());

            final List<GuiItem> items = this.getItems(identifier);
            for (final GuiItem item : items) { /* For each of these items apply a new item */
                final GuiItem newItem = this.createGuiItem(item.getSlot(), item.getTemplate());
                this.setItem(item.getSlot(), newItem);
            }
        }
    }

    @Override
    public GuiTemplate getTemplate() {
        return this.template;
    }

    @Override
    public void remapItems(String identifier, Processor<ItemBuilder> modifier, long cacheFor) {
        final Processor<ItemStack> processor = (item) -> modifier.apply(ItemBuilder.create(item)).build();
        this.processors.put(identifier, new CachedGuiItem(processor, cacheFor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> GuiPagination<T> getPagination() {
        return (GuiPagination<T>) this.pagination;
    }

    @Override
    public <T> void createPagination(GuiPagination<T> pagination) {
        this.pagination = pagination;
    }

    @Override
    public Collection<GuiItem> getItems() {
        return this.items.values();
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        final int row = slot / 9;
        if (row >= this.inventory.getRows()) {
            return;
        }

        this.items.put(slot, item);
        this.update(slot, item);
    }

    @Override
    public List<GuiItem> getItems(String identifier) {
        return Iterables.query(this.items.values(), (item) -> item.getTemplate().getIdentifier(), identifier);
    }

    @Override
    public void addClickHandler(String identifier, Consumer<ClickEvent> handler) {
        this.clickHandlers.put(identifier, handler);
    }

    @Override
    public void handleClick(String identifier, ClickEvent event) {
        final Consumer<ClickEvent> handler = this.clickHandlers.get(identifier);
        if (handler == null) {
            return;
        }

        handler.accept(event);
    }

    @Override
    public Optional<GuiItem> getItem(int slot) {
        return Optional.ofNullable(this.items.get(slot));
    }

    @Override
    public Optional<ItemStack> getItemStack(int slot) {
        return this.getItem(slot).map(GuiItem::getItemStack);
    }

    private void update(int slot, GuiItem item) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) { // Shouldn't happen but in case
            return;
        }

        final GuiManager manager = GuiManager.get();
        if (!manager.hasInventory(player, this.inventory)) {
            return;
        }

        final Inventory topInv = player.getOpenInventory().getTopInventory();
        topInv.setItem(slot, item.getItemStack());
        player.updateInventory();
    }
}
