package io.pinger.plus.storage.implementation.sql;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;

public class SqlStorage implements StorageImplementation {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public ConnectionFactory getConnectionSourceProvider() {
        return this.connectionFactory;
    }

    @Override
    public void init() throws Exception {
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
