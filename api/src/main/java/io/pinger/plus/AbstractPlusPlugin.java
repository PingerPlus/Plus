package io.pinger.plus;

import io.pinger.plus.dependency.Dependency;
import io.pinger.plus.dependency.DependencyManager;
import io.pinger.plus.event.EventManager;
import io.pinger.plus.event.EventRegistry;
import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.scheduler.Schedulers;
import java.util.Set;

public abstract class AbstractPlusPlugin implements PlusPlugin {
    private DependencyManager dependencyManager;

    private EventManager<?> eventManager;
    private Schedulers schedulers;

    public final void loadInternally() {
        this.dependencyManager = this.createDependencyManager();
        this.dependencyManager.addGlobalRepositories();
        this.dependencyManager.loadDependencies(this.getDependencies());

        Instances.register(this.getLogger());
    }

    public final void enableInternally() {
        // Initialize event handler
        Instances.register(new EventRegistry());

        this.eventManager = this.createEventManager();
        this.schedulers = this.createSchedulers();

        EventRegistry.get().registerEventManager(this.eventManager);
        Instances.register(this.schedulers);
    }

    public final void disableInternally() {
        this.getLogger().info("Goodbye!");
    }

    protected abstract DependencyManager createDependencyManager();
    protected abstract EventManager<?> createEventManager();
    protected abstract Schedulers createSchedulers();
    protected abstract Set<Dependency> getDependencies();

    @Override
    public PluginLogger getLogger() {
        return this.getBootstrap().getLogger();
    }

    @Override
    public DependencyManager getDependencyManager() {
        return this.dependencyManager;
    }

    @Override
    public EventManager<?> getEventManager() {
        return this.eventManager;
    }

    @Override
    public Schedulers getSchedulers() {
        return this.schedulers;
    }
}
