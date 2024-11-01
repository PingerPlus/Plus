package io.pinger.plus.dependency;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import io.pinger.plus.storage.type.StorageType;

public enum Dependencies implements Dependency {

    HIKARI(
        "com.zaxxer",
        "HikariCP",
        "4.0.3"
    ),
    MARIA_DB(
        "org.mariadb.jdbc",
        "mariadb-java-client",
        "3.4.1"
    ),
    MYSQL_DRIVER(
        "mysql",
        "mysql-connector-java",
        "8.0.23"
    ),
    POSTGRESQL_DRIVER(
        "org.postgresql",
        "postgresql",
        "42.6.0"
    );

    public static final SetMultimap<StorageType, Dependency> STORAGE_DEPENDENCIES = ImmutableSetMultimap.<StorageType, Dependency>builder()
        .putAll(StorageType.MYSQL, Dependencies.HIKARI, Dependencies.MYSQL_DRIVER)
        .putAll(StorageType.MARIADB, Dependencies.HIKARI, Dependencies.MARIA_DB)
        .putAll(StorageType.POSTGRESQL, Dependencies.HIKARI, Dependencies.POSTGRESQL_DRIVER)
        .build();

    private final String groupId;
    private final String artifactId;
    private final String version;

    Dependencies(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public String groupId() {
        return this.groupId;
    }

    @Override
    public String artifactId() {
        return this.artifactId;
    }

    @Override
    public String version() {
        return this.version;
    }
}
