package io.pinger.plus.storage.implementation.sql;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;
import java.io.InputStream;

public abstract class AbstractSqlStorage implements StorageImplementation {
    private final ConnectionFactory connectionFactory;
    private final PluginLogger logger;

    protected AbstractSqlStorage(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.logger = Instances.get(PluginLogger.class);
    }

    @Override
    public void init() {
        this.connectionFactory.init();

        final String resourceName = String.format("%s.sql", this.connectionFactory.getStorageType().getIdentifier());

        this.logger.info("Trying to create default tables from file {}", resourceName);

        final InputStream inputStream = this.getResource(resourceName);
        SqlStorageLoader.init(this.connectionFactory, inputStream);

        this.logger.info("Successfully created default tables from file {}", resourceName);
    }

    @Override
    public void shutdown() {
        try {
            this.connectionFactory.shutdown();
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Exception occurred while shutting down database {}", e);
        }
    }

    private InputStream getResource(String resourceName) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceName);
    }
}
