package io.pinger.plus.storage.implementation;

import com.j256.ormlite.support.ConnectionSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSourceProvider {

    Connection getConnection() throws SQLException;

    ConnectionSource getConnectionSource();

}
