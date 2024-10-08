package io.pinger.plus.storage.implementation.sql.connection.hikari;

import io.pinger.plus.storage.credentials.StorageCredentials;
import java.util.Map;
import java.util.function.Function;

public class PostgresConnectionFactory extends HikariConnectionFactory {

    public PostgresConnectionFactory(StorageCredentials configuration) {
        super(configuration);
    }

    @Override
    public String getImplementationName() {
        return "PostgreSQL";
    }

    @Override
    protected String defaultPort() {
        return "5432";
    }

    @Override
    protected String driverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected String driverJdbcIdentifier() {
        return "postgresql";
    }

    @Override
    protected void overrideProperties(Map<String, Object> properties) {
        super.overrideProperties(properties);

        // remove the default config properties which don't exist for PostgreSQL
        properties.remove("useUnicode");
        properties.remove("characterEncoding");
    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace('\'', '"');
    }
}
