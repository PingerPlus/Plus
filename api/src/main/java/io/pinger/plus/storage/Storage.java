package io.pinger.plus.storage;

import io.pinger.plus.scheduler.Workers;
import io.pinger.plus.storage.implementation.StorageImplementation;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Storage {

    StorageImplementation getImplementation();

    default <T> CompletableFuture<T> future(Callable<T> callable) {
        return Workers.get().callAsync(callable);
    }

    default <T> CompletableFuture<T> future(Supplier<T> supplier) {
        return Workers.get().supplyAsync(supplier);
    }

}
