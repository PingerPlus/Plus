package io.pinger.plus.spigot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.platform.Platform;
import io.pinger.plus.plugin.bootstrap.LoaderBootstrap;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.spigot.plugin.logging.SpigotPluginLogger;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBootstrap implements Bootstrap, LoaderBootstrap {
    private final PluginLogger logger;
    private final JavaPlugin loader;

    private Injector injector;

    public PluginBootstrap(JavaPlugin plugin) {
        this.loader = plugin;
        this.logger = new SpigotPluginLogger(plugin.getLogger());
    }

    @Override
    public final void onLoad() {
        this.injector = Guice.createInjector(this.getModule());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        
    }

    public JavaPlugin getLoader() {
        return this.loader;
    }

    @Override
    public PluginLogger getLogger() {
        return this.logger;
    }

    @Override
    public final Injector getInjector() {
        return this.injector;
    }

    @Override
    public Platform getPlatform() {
        return Platform.SPIGOT;
    }

    @Override
    public Path getDataDirectory() {
        return this.loader.getDataFolder().toPath().toAbsolutePath();
    }
}
