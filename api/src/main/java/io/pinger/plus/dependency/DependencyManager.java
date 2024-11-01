package io.pinger.plus.dependency;

import io.pinger.plus.storage.type.StorageType;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface DependencyManager {

    void addRepositories(@NotNull Set<String> repositories);

    void addGlobalRepositories();

    default void loadDependencies(@NotNull Set<Dependency> dependencies) {
        dependencies.forEach(this::loadDependency);
    }

    void loadDependency(@NotNull Dependency dependency);

    void loadStorageDependencies(StorageType storageType);

}
