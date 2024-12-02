package io.pinger.plus.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.j256.ormlite.support.ConnectionSource;
import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.storage.implementation.ConnectionSourceProvider;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;
import io.pinger.plus.storage.type.StorageType;
import java.sql.Connection;
import java.sql.SQLException;

public class StorageModule extends AbstractModule {
    private final StorageFactory factory;
    private final StorageType type;

    public StorageModule(StorageFactory factory, StorageType type) {
        this.factory = factory;
        this.type = type;
    }

    @Override
    protected void configure() {
        final StorageImplementation implementation = this.factory.createStorageImplementation(this.type);

        try {
            implementation.init();
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Failed to init storage: ", e);
            return;
        }

        this.bind(StorageImplementation.class).toInstance(implementation);
        this.bind(ConnectionSourceProvider.class).toInstance(implementation.getConnectionSourceProvider());
    }

    @Provides
    public ConnectionSource provideConnectionSource(ConnectionSourceProvider provider) {
        return provider.getConnectionSource();
    }
}
