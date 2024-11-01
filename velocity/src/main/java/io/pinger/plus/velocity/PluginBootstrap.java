package io.pinger.plus.velocity;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.platform.Platform;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.velocity.plugin.logging.VelocityPluginLogger;
import java.nio.file.Path;
import org.slf4j.Logger;

public abstract class PluginBootstrap implements Bootstrap {
    private final PluginLogger logger;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private Injector injector;

    @Inject
    public PluginBootstrap(Logger logger) {
        this.logger = new VelocityPluginLogger(logger);
    }

    @Subscribe
    public void onProxyStart(ProxyInitializeEvent event) {
        this.injector = Guice.createInjector(this.getModule());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

    public ProxyServer getProxyServer() {
        return this.proxyServer;
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
        return Platform.VELOCITY;
    }

    @Override
    public Path getDataDirectory() {
        return this.dataDirectory;
    }
}
