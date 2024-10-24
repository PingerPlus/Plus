package io.pinger.plus.storage.implementation.sql.connection.hikari;

import io.pinger.plus.storage.type.StorageType;
import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.util.Processor;

public class MariaDbConnectionFactory extends HikariConnectionFactory {

    public MariaDbConnectionFactory(StorageConfig credentials) {
        super(credentials);
    }

    @Override
    protected String getDefaultPort() {
        return "3306";
    }

    @Override
    protected String getDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    protected String getDriverIdentifier() {
        return "mariadb";
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.MARIADB;
    }

    @Override
    public Processor<String> getStatementProcessor() {
        return s -> s.replace('\'', '`');
    }
}
