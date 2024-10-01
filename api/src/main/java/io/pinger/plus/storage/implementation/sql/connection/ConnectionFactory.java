package io.pinger.plus.storage.implementation.sql.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public interface ConnectionFactory {

    String getImplementationName();

    void init();

    void shutdown() throws Exception;

    Connection getConnection() throws SQLException;

    Function<String, String> getStatementProcessor();

}
