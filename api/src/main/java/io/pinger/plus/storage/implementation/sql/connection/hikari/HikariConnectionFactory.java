package io.pinger.plus.storage.implementation.sql.connection.hikari;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnectionFactory implements ConnectionFactory {
    private final StorageConfig credentials;

    private HikariDataSource source;
    private ConnectionSource connectionSource;

    protected HikariConnectionFactory(StorageConfig credentials) {
        this.credentials = credentials;
    }

    protected abstract String getDefaultPort();

    protected abstract String getDriverClassName();

    protected abstract String getDriverIdentifier();

    @Override
    public void init() throws SQLException {
        final HikariConfig config = new HikariConfig();
        config.setPoolName("plus-pool");

        final String connectionUrl = this.getConnectionURL();

        config.setJdbcUrl(connectionUrl);
        config.setDriverClassName(this.getDriverClassName());
        config.setUsername(this.credentials.getUsername());
        config.setPassword(this.credentials.getPassword());

        config.setMaximumPoolSize(this.credentials.getMaxPoolSize());
        config.setMinimumIdle(this.credentials.getMinIdleConnections());
        config.setMaxLifetime(this.credentials.getMaxLifetime());
        config.setKeepaliveTime(this.credentials.getKeepAliveTime());
        config.setConnectionTimeout(this.credentials.getConnectionTimeout());

        this.source = new HikariDataSource(config);
        this.connectionSource = new DataSourceConnectionSource(this.source, connectionUrl);
    }

    @Override
    public void shutdown() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.source.getConnection();
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return this.connectionSource;
    }

    private String getConnectionURL() {
        final String[] split = this.credentials.getAddress().split(":");
        final String host = split[0];
        final String port = split.length > 1 ? split[1] : this.getDefaultPort();

        return String.format(
            "jdbc:%s://%s:%s/%s",
            this.getDriverIdentifier(),
            host,
            port,
            this.credentials.getDatabase()
        );
    }
}
