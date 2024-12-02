package io.pinger.plus.spigot;

import io.pinger.plus.AbstractPlusPlugin;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.dependency.Dependency;
import io.pinger.plus.dependency.DependencyManager;
import io.pinger.plus.event.EventManager;
import io.pinger.plus.scheduler.Schedulers;
import io.pinger.plus.spigot.dependency.SimpleDependencyManager;
import io.pinger.plus.spigot.event.SpigotEventManager;
import io.pinger.plus.spigot.scheduler.SpigotAsyncScheduler;
import io.pinger.plus.spigot.scheduler.SpigotSyncScheduler;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPlugin extends AbstractPlusPlugin {
    private final PluginBootstrap bootstrap;

    public AbstractPlugin(PluginBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected DependencyManager createDependencyManager() {
        return new SimpleDependencyManager(this.bootstrap);
    }

    @Override
    protected EventManager<?> createEventManager() {
        return new SpigotEventManager();
    }

    @Override
    protected Schedulers createSchedulers() {
        return new Schedulers(
            new SpigotSyncScheduler(this.bootstrap.getLoader()),
            new SpigotAsyncScheduler(this.bootstrap.getLoader())
        );
    }

    @Override
    protected Set<Dependency> getDependencies() {
        return new HashSet<>();
    }

    @Override
    public Bootstrap getBootstrap() {
        return this.bootstrap;
    }
}
