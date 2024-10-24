package io.pinger.plus.storage.implementation.sql.connection;

import io.pinger.plus.storage.type.StorageType;
import io.pinger.plus.util.Processor;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    StorageType getStorageType();

    void init();

    void shutdown();

    Connection getConnection() throws SQLException;

    Processor<String> getStatementProcessor();

}
