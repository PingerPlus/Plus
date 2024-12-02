package io.pinger.plus.storage;

import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.implementation.sql.SqlStorage;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;
import io.pinger.plus.storage.implementation.sql.connection.hikari.MariaDbConnectionFactory;
import io.pinger.plus.storage.implementation.sql.connection.hikari.MySqlConnectionFactory;
import io.pinger.plus.storage.implementation.sql.connection.hikari.PostgresConnectionFactory;
import io.pinger.plus.storage.type.StorageType;

public class StorageFactory {
    private final StorageConfig config;

    public StorageFactory(StorageConfig config) {
        this.config = config;
    }

    public StorageImplementation createStorageImplementation(StorageType type) {
        switch (type) {
            case MARIADB:
            case MYSQL:
            case POSTGRESQL: return new SqlStorage(this.createConnectionFactory(type));
            default: throw new IllegalStateException("Unknown storage type: " + type);
        }
    }

    protected ConnectionFactory createConnectionFactory(StorageType type) {
        switch (type) {
            case MYSQL: return new MySqlConnectionFactory(this.config);
            case MARIADB: return new MariaDbConnectionFactory(this.config);
            case POSTGRESQL: return new PostgresConnectionFactory(this.config);
            default: throw new IllegalStateException("Unknown storage type: " + type);
        }
    }

}
