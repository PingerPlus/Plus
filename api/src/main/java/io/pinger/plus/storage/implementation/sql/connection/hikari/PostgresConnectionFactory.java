package io.pinger.plus.storage.implementation.sql.connection.hikari;

import io.pinger.plus.storage.type.StorageType;
import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.util.Processor;

public class PostgresConnectionFactory extends HikariConnectionFactory {

    public PostgresConnectionFactory(StorageConfig credentials) {
        super(credentials);
    }

    @Override
    protected String getDefaultPort() {
        return "5432";
    }

    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected String getDriverIdentifier() {
        return "postgresql";
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.POSTGRESQL;
    }

    @Override
    public Processor<String> getStatementProcessor() {
        return s -> s.replace('\'', '"');
    }
}
