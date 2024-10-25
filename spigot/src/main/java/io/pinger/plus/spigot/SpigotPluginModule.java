package io.pinger.plus.spigot;

import io.pinger.plus.AbstractBootstrap;
import io.pinger.plus.PluginModule;

public abstract class SpigotPluginModule extends PluginModule {
    private final

    public SpigotPluginModule(AbstractBootstrap bootstrap) {
        super(bootstrap);
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind()
    }
}
