package io.pinger.plus.storage.implementation.sql;

import io.pinger.plus.instance.Instances;
import io.pinger.plus.plugin.logging.PluginLogger;
import io.pinger.plus.storage.implementation.sql.connection.ConnectionFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;

public class SqlStorageLoader {

    public static void init(ConnectionFactory factory, InputStream stream) {
        final String contents;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            contents = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Instances.get(PluginLogger.class).error("Failed to read contents from source file", e);
            return;
        }

        final String[] queries = contents.split(";");
        try (final Connection connection = factory.getConnection()) {
            for (final String query : queries) {
                if (query.trim().isEmpty()) {
                    continue;
                }

                try (final PreparedStatement stat = connection.prepareStatement(query)) {
                    stat.execute();
                }
            }
        } catch (Exception e) {
            Instances.get(PluginLogger.class).error("Failed to execute a query", e);
        }
    }

}
