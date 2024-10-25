package io.pinger.plus.spigot;

import io.pinger.plus.Bootstrap;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.spigot.plugin.logging.SpigotPluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBootstrap implements Bootstrap {
    private final PluginLogger logger;

    protected final JavaPlugin plugin;

    public PluginBootstrap(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = new SpigotPluginLogger(plugin.getLogger());
    }

    public JavaPlugin getLoader() {
        return this.plugin;
    }

    @Override
    public PluginLogger getLogger() {
        return this.logger;
    }
}
