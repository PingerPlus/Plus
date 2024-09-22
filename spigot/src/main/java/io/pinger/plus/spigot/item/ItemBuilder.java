package io.pinger.plus.spigot.item;

import io.pinger.plus.message.ComponentHelper;
import io.pinger.plus.message.ComponentSerializer;
import io.pinger.plus.spigot.material.XMaterial;
import io.pinger.plus.text.Replacer;
import io.pinger.plus.text.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(@Nonnull ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(@Nonnull Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(@Nonnull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(XMaterial material) {
        this(material.parseItem());
    }

    public ItemBuilder() {
        this.item = new ItemStack(Material.AIR);
    }

    public static ItemBuilder create(ItemStack item) {
        return new ItemBuilder(item);
    }

    public static ItemBuilder create(XMaterial material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder create(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    private ItemBuilder transform(Consumer<ItemStack> consumer) {
        consumer.accept(this.item);
        return this;
    }

    private ItemBuilder transformMeta(Consumer<ItemMeta> consumer) {
        final ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            consumer.accept(meta);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder type(Material material) {
        return this.transform((item) -> item.setType(material));
    }

    public ItemBuilder type(XMaterial material) {
        final ItemBuilder cloneBuilder = ItemBuilder.create(material);
        final ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return cloneBuilder;
        }

        if (meta.hasDisplayName()) {
            cloneBuilder.name(meta.getDisplayName());
        }

        if (meta.hasLore()) {
            cloneBuilder.lore(meta.getLore());
        }

        if (meta.hasEnchants()) {
            for (final Entry<Enchantment, Integer> enchantEntry : meta.getEnchants().entrySet()) {
                cloneBuilder.enchant(enchantEntry.getKey(), enchantEntry.getValue());
            }
        }

        for (final ItemFlag itemFlag : meta.getItemFlags()) {
            cloneBuilder.flag(itemFlag);
        }

        this.item = cloneBuilder.build();
        return this;
    }

    public ItemBuilder amount(int amount) {
        return this.transform((item) -> item.setAmount(amount));
    }

    public ItemBuilder name(@Nonnull String name) {
        return this.transformMeta((meta) -> meta.setDisplayName(Text.colorize(name)));
    }

    public ItemBuilder lore(Iterable<String> lines) {
        return this.transformMeta((meta) -> {
            final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            for (final String line : lines) {
                lore.add(Text.colorize(line));
            }
            meta.setLore(lore);
        });
    }

    public ItemBuilder lore(String line) {
        return this.transformMeta((meta) -> {
            final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            lore.add(Text.colorize(line));
            meta.setLore(lore);
        });
    }

    public ItemBuilder replace(@NotNull Replacer replacer) {
        final ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return this;
        }

        if (meta.hasDisplayName()) {
            final Component oldDisplayName = ComponentSerializer.componentFromContent(meta.getDisplayName());
            final Component newDisplayName = replacer.accept(oldDisplayName);
            if (!ComponentHelper.isEmpty(newDisplayName)) {
                meta.setDisplayName(ComponentSerializer.contentFromComponent(newDisplayName));
            }
            this.item.setItemMeta(meta);
        }

        if (meta.hasLore()) {
            final List<Component> lore = replacer.accept(ItemHandler.getItemLore(this.item));
            ItemHandler.setItemLore(this.item, lore);
        }
        return this;
    }

    public ItemBuilder lore(String... lines) {
        return this.lore(Arrays.asList(lines));
    }

    public ItemBuilder flag(ItemFlag... flags) {
        return this.transformMeta((meta) -> meta.addItemFlags(flags));
    }

    public ItemBuilder flag() {
        return this.flag(ItemFlag.values());
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        return this.transform(item -> item.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        return this.enchant(enchantment, 1);
    }

    public ItemBuilder glow() {
        return this.enchant(Enchantment.UNBREAKING, 0);
    }

    public ItemStack build() {
        return this.item;
    }

}
