package io.pinger.plus.spigot;

import io.pinger.plus.PluginModule;
import io.pinger.plus.spigot.gui.GuiManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPluginModule extends PluginModule {
    private final PluginBootstrap bootstrap;

    public SpigotPluginModule(PluginBootstrap bootstrap) {
        super(bootstrap);

        this.bootstrap = bootstrap;
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind(Plugin.class).to(JavaPlugin.class);
        this.bind(JavaPlugin.class).toInstance(this.bootstrap.getLoader());
        this.bind(GuiManager.class).asEagerSingleton();

        this.configurePlugin();
    }
}
