package io.pinger.plus.spigot;

import io.pinger.plus.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

public class DPBootstrap extends PluginBootstrap {

    public DPBootstrap(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public PluginModule getModule() {
        return null;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
