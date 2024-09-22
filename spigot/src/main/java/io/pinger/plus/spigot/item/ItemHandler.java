package io.pinger.plus.spigot.item;

import io.pinger.plus.message.ComponentSerializer;
import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemHandler {

    public static void setItemLore(@NotNull ItemStack itemStack, @NotNull List<Component> lore) {
        final List<String> newLore = new LinkedList<>();
        final List<Component> stuff = new LinkedList<>();

        if (itemStack.getType() == Material.AIR) {
            return;
        }

        for (final Component component : lore) {
            boolean newLine = false;
            boolean anyMatch = false;

            for (final Component child : component.children()) {
                if (child.equals(Component.newline())) {
                    newLine = true;
                    anyMatch = true;
                    continue;
                }

                if (newLine) {
                    newLine = false;
                    stuff.add(child);
                }
            }

            if (!anyMatch) {
                stuff.add(component);
            }
        }

        for (final Component component : stuff) {
            newLore.add(ComponentSerializer.contentFromComponent(component));
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static List<Component> getItemLore(@NotNull ItemStack itemStack) {
        final List<Component> lore = new LinkedList<>();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return lore;
        }

        if (!itemMeta.hasLore()) {
            return lore;
        }

        for (final String line : itemMeta.getLore()) {
            lore.add(ComponentSerializer.componentFromContent(line));
        }

        return lore;
    }


}
