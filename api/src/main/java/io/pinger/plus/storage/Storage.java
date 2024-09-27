package io.pinger.plus.storage;

import io.pinger.plus.scheduler.Workers;
import io.pinger.plus.storage.implementation.StorageImplementation;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class Storage {
    private final StorageImplementation implementation;

    public Storage(StorageImplementation implementation) {
        this.implementation = implementation;
    }

    @SuppressWarnings("unchecked")
    protected <T extends StorageImplementation> T implementation() {
        return (T) this.implementation;
    }

    public <T> CompletableFuture<T> future(Callable<T> callable) {
        return Workers.get().callAsync(callable);
    }

    public <T> CompletableFuture<T> future(Supplier<T> supplier) {
        return Workers.get().supplyAsync(supplier);
    }

}
