package io.pinger.plus.orm;

import com.j256.ormlite.dao.Dao;
import lombok.SneakyThrows;

public interface Repository<T, ID> extends Dao<T, ID> {

    @SneakyThrows
    default void sneakyCreate(T data) {
        this.create(data);
    }

    @SneakyThrows
    default void sneakyCreateOrUpdate(T data) {
        this.createOrUpdate(data);
    }

    @SneakyThrows
    default void sneakyUpdate(T data) {
        this.update(data);
    }

    @SneakyThrows
    default void sneakyDelete(T data) {
        this.delete(data);
    }

}
