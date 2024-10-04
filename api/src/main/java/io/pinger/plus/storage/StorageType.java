package io.pinger.plus.storage;

public enum StorageType {

    MONGODB("MongoDB", "mongodb"),
    MARIADB("MariaDB", "mariadb"),
    MYSQL("MySQL", "mysql"),
    POSTGRESQL("PostgreSQL", "postgresql");

    private final String name;
    private final String identifier;

    StorageType(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
