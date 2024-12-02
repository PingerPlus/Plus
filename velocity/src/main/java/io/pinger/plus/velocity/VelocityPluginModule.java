package io.pinger.plus.velocity;

import io.pinger.plus.PluginModule;

public abstract class VelocityPluginModule extends PluginModule {
    private final PluginBootstrap bootstrap;

    public VelocityPluginModule(PluginBootstrap bootstrap) {
        super(bootstrap);

        this.bootstrap = bootstrap;
    }

    @Override
    protected void configure() {
        super.configure();
    }
}
