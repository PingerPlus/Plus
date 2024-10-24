package io.pinger.plus.storage.implementation.sql.connection.hikari;

import io.pinger.plus.storage.type.StorageType;
import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.util.Processor;

public class MySqlConnectionFactory extends HikariConnectionFactory {

    public MySqlConnectionFactory(StorageConfig credentials) {
        super(credentials);
    }

    @Override
    protected String getDefaultPort() {
        return "3306";
    }

    @Override
    protected String getDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    protected String getDriverIdentifier() {
        return "mysql";
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.MYSQL;
    }

    @Override
    public Processor<String> getStatementProcessor() {
        return s -> s.replace('\'', '`');
    }
}
