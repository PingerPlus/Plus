package io.pinger.plus.orm;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;

public interface Entities {

    @SneakyThrows
    static <T> void createIfNotExists(ConnectionSource connectionSource, Class<T> entityClass) {
        TableUtils.createTableIfNotExists(connectionSource, entityClass);
    }

}
