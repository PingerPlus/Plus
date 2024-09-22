package io.pinger.plus.spigot.gui.item;

import io.pinger.plus.util.Processor;
import org.bukkit.inventory.ItemStack;

public class CachedGuiItem {
    private final Processor<ItemStack> processor;

    private long cacheFor = -1;
    private long lastCached = -1;

    public CachedGuiItem(Processor<ItemStack> processor) {
        this.processor = processor;
    }

    public CachedGuiItem(Processor<ItemStack> processor, long cacheFor) {
        this.processor = processor;
        this.cacheFor = cacheFor;
    }

    public boolean isCacheEnabled() {
        return this.cacheFor >= 0;
    }

    public Processor<ItemStack> getProcessor() {
        return this.processor;
    }

    public long getCacheFor() {
        return this.cacheFor;
    }

    public long getLastCached() {
        return this.lastCached;
    }

    public void setLastCached(long lastCached) {
        this.lastCached = lastCached;
    }

}
