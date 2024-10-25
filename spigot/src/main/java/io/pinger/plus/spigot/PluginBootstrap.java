package io.pinger.plus.spigot;

import io.pinger.plus.AbstractBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBootstrap extends AbstractBootstrap {
    protected final JavaPlugin plugin;

    public PluginBootstrap(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
