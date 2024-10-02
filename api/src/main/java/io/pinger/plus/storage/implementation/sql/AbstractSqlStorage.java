package io.pinger.plus.storage.implementation.sql;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;

public abstract class AbstractSqlStorage implements StorageImplementation {
    private final ConnectionFactory connectionFactory;

    protected AbstractSqlStorage(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void init() {
        this.connectionFactory.init();
    }

    @Override
    public void shutdown() {
        try {
            this.connectionFactory.shutdown();
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Exception occurred while shutting down database {}", e);
        }
    }
}
