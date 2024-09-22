package io.pinger.plus.spigot.item;

import io.pinger.plus.spigot.material.XMaterial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemSerializer {

    public static @NotNull Map<String, Object> serialize(@NotNull ItemStack itemStack) {
        final Map<String, Object> map = new HashMap<>();

        map.put("type", itemStack.getType().toString());

        if (itemStack.getAmount() > 1) {
            map.put("amount", itemStack.getAmount());
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            if (itemMeta.hasDisplayName()) {
                map.put("displayName", itemMeta.getDisplayName());
            }

            if (itemMeta.hasLore()) {
                map.put("lore", itemMeta.getLore());
            }

            if (itemMeta.hasEnchant(Enchantment.UNBREAKING)) {
                map.put("glow", true);
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public static @NotNull ItemStack deserialize(@NotNull Map<String, Object> map) {
        if (!map.containsKey("type")) {
            throw new IllegalArgumentException("The given map does not contain a valid material type");
        }

        final Optional<XMaterial> material = XMaterial.matchXMaterial(map.get("type").toString());
        if (!material.isPresent()) {
            throw new IllegalStateException("Item type not found!" + map.get("type"));
        }

        final ItemBuilder builder = new ItemBuilder(material.get());

        if (map.containsKey("amount")) {
            builder.amount(Integer.parseInt(map.get("amount").toString()));
        }

        if (map.containsKey("displayName")) {
            builder.name(map.get("displayName").toString());
        }

        if (map.containsKey("lore")) {
            builder.lore((List<String>) map.get("lore"));
        }

        if (map.containsKey("glow")) {
            builder.glow();
        }

        return builder.build();
    }


}
