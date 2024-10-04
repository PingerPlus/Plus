package io.pinger.plus.storage;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;

public abstract class StorageFactory {
    private final PluginLogger logger;

    public StorageFactory() {
        this.logger = Instances.get(PluginLogger.class);
    }

    public Storage getStorage() {
        this.logger.info("Trying to ");
        final StorageType type = null;
        return this.createImplementation(type);
    }

    public abstract Storage createImplementation(StorageType type);

}
