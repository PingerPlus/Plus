package io.pinger.plus.storage;

import com.google.inject.AbstractModule;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.type.StorageType;

public class StorageModule<T extends StorageImplementation> extends AbstractModule {
    private final AbstractStorageFactory factory;
    private final StorageType type;
    private final Class<T> rootClass;
    private final Class<? extends Storage> storageClass;

    public StorageModule(AbstractStorageFactory factory, StorageType type, Class<T> rootClass, Class<? extends Storage> storageClass) {
        this.factory = factory;
        this.type = type;
        this.rootClass = rootClass;
        this.storageClass = storageClass;
    }

    @Override
    protected void configure() {
        final StorageImplementation storage = this.factory.createStorage(this.type);
        this.bind(this.rootClass).toInstance(this.rootClass.cast(storage));
        this.bind(Storage.class).to(this.storageClass);
    }
}
