package io.pinger.plus.storage.implementation.sql.connection;

import io.pinger.plus.storage.implementation.ConnectionSourceProvider;
import io.pinger.plus.storage.type.StorageType;
import io.pinger.plus.util.Processor;

public interface ConnectionFactory extends ConnectionSourceProvider {

    StorageType getStorageType();

    void init() throws Exception;

    void shutdown() throws Exception;

    Processor<String> getStatementProcessor();

}
