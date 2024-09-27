package io.pinger.plus.storage.implementation.sql;

import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;

public abstract class SqlStorage implements StorageImplementation {
    private final ConnectionFactory connectionFactory;

    protected SqlStorage(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void init() {
        this.connectionFactory.init();

    }

    @Override
    public void shutdown() {

    }
}
