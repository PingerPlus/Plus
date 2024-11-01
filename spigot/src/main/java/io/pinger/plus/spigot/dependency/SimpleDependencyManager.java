package io.pinger.plus.spigot.dependency;

import io.pinger.plus.dependency.AbstractDependencyManager;
import io.pinger.plus.spigot.PluginBootstrap;
import net.byteflux.libby.BukkitLibraryManager;

public class SimpleDependencyManager extends AbstractDependencyManager {

    public SimpleDependencyManager(PluginBootstrap bootstrap) {
        super(new BukkitLibraryManager(bootstrap.getLoader()));
    }

}
