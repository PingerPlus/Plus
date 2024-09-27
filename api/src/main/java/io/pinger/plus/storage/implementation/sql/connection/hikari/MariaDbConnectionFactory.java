package io.pinger.plus.storage.implementation.sql.connection.hikari;

import io.pinger.plus.storage.credentials.StorageCredentials;

public class MariaDbConnectionFactory extends DriverBasedHikariConnectionFactory {
    public MariaDbConnectionFactory(StorageCredentials configuration) {
        super(configuration);
    }

    @Override
    public String getImplementationName() {
        return "MariaDB";
    }

    @Override
    protected String defaultPort() {
        return "3306";
    }

    @Override
    protected String driverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    protected String driverJdbcIdentifier() {
        return "mariadb";
    }

}
