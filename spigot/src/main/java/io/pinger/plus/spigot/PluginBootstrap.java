package io.pinger.plus.spigot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.PluginModule;
import io.pinger.plus.classpath.ClassScanner;
import io.pinger.plus.platform.Platform;
import io.pinger.plus.plugin.bootstrap.LoaderBootstrap;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.spigot.gui.GuiManager;
import io.pinger.plus.spigot.plugin.logging.SpigotPluginLogger;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBootstrap implements Bootstrap, LoaderBootstrap {
    private final AbstractPlugin plugin;
    private final PluginLogger logger;
    private final JavaPlugin loader;
    private final ClassScanner scanner;

    private Injector injector;
    private GuiManager guiManager;

    public PluginBootstrap(JavaPlugin plugin) {
        this.loader = plugin;
        this.logger = new SpigotPluginLogger(plugin.getLogger());
        this.plugin = this.withPlugin();
        this.scanner = new ClassScanner(this);
    }

    @Override
    public final void onLoad() {
        try {
            this.plugin.loadInternally();
        } catch (Exception e) {
            this.logger.error("Failed to load plugin", e);
        }

        this.injector = Guice.createInjector(this.getModule());
    }

    @Override
    public void onEnable() {
        try {
            this.plugin.enableInternally();
        } catch (Exception e) {
            this.logger.error("Failed to enable plugin", e);
        }

        this.guiManager = this.get(GuiManager.class);
    }

    @Override
    public void onDisable() {
        try {
            this.plugin.disableInternally();
        } catch (Exception e) {
            this.logger.error("Failed to disable plugin", e);
        }
    }

    @Override
    public final ClassScanner getClassScanner() {
        return this.scanner;
    }

    public abstract AbstractPlugin withPlugin();

    public final JavaPlugin getLoader() {
        return this.loader;
    }

    public final GuiManager getGuiManager() {
        return this.guiManager;
    }

    @Override
    public final AbstractPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public final PluginLogger getLogger() {
        return this.logger;
    }

    @Override
    public final Platform getPlatform() {
        return Platform.SPIGOT;
    }

    @Override
    public final Injector getInjector() {
        return this.injector;
    }

    @Override
    public final Path getDataDirectory() {
        return this.loader.getDataFolder().toPath().toAbsolutePath();
    }
}
