package io.pinger.plus.storage.implementation.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import io.pinger.plus.storage.credentials.StorageCredentials;

public abstract class DriverBasedHikariConnectionFactory extends HikariConnectionFactory {

    protected DriverBasedHikariConnectionFactory(StorageCredentials configuration) {
        super(configuration);
    }

    protected abstract String driverClassName();

    protected abstract String driverJdbcIdentifier();

    @Override
    protected void configureDatabase(HikariConfig config, String address, String port, String databaseName, String username, String password) {
        config.setDriverClassName(this.driverClassName());
        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", this.driverJdbcIdentifier(), address, port, databaseName));
        config.setUsername(username);
        config.setPassword(password);
    }

}
