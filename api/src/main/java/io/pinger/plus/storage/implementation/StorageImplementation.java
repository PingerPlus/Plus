package io.pinger.plus.storage.implementation;

public interface StorageImplementation {

    ConnectionSourceProvider getConnectionSourceProvider();

    void init() throws Exception;

    void shutdown();

}
