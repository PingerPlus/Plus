package io.pinger.plus.spigot.plugin;

import io.pinger.plus.plugin.bootstrap.BootstrapLoader;
import io.pinger.plus.plugin.bootstrap.LoaderBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLoader extends JavaPlugin {
    private final LoaderBootstrap bootstrap;

    public PluginLoader() {
        final BootstrapLoader loader = new BootstrapLoader();
        this.bootstrap = loader.loadBootstrap(JavaPlugin.class, this);
    }

    @Override
    public void onLoad() {
        this.bootstrap.onLoad();
    }

    @Override
    public void onEnable() {
        this.bootstrap.onEnable();
    }

    @Override
    public void onDisable() {
        this.bootstrap.onDisable();
    }
}
