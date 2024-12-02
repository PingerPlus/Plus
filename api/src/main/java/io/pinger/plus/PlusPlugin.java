package io.pinger.plus;

import io.pinger.plus.dependency.DependencyManager;
import io.pinger.plus.event.EventManager;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.scheduler.Schedulers;
import net.byteflux.libby.Library;

public interface PlusPlugin {

    Bootstrap getBootstrap();

    PluginLogger getLogger();

    DependencyManager getDependencyManager();

    EventManager<?> getEventManager();

    Schedulers getSchedulers();
}
