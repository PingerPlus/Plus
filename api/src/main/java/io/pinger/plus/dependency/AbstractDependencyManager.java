package io.pinger.plus.dependency;

import io.pinger.plus.storage.type.StorageType;
import java.util.Set;
import net.byteflux.libby.LibraryManager;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDependencyManager implements DependencyManager {
    private final LibraryManager manager;

    public AbstractDependencyManager(LibraryManager manager) {
        this.manager = manager;
    }

    @Override
    public void addGlobalRepositories() {
        this.manager.addMavenCentral();
        this.manager.addSonatype();
    }

    @Override
    public void addRepositories(@NotNull Set<String> repositories) {
        repositories.forEach(this.manager::addRepository);
    }

    @Override
    public void loadDependency(@NotNull Dependency dependency) {
        this.manager.loadLibrary(dependency.toLibrary());
    }

    @Override
    public void loadStorageDependencies(StorageType storageType) {
        this.loadDependencies(Dependencies.STORAGE_DEPENDENCIES.get(storageType));
    }
}
