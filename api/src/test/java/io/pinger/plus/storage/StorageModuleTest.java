package io.pinger.plus.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.annotation.RuntimeInitialized;
import io.pinger.plus.inject.ClassMatcher;
import io.pinger.plus.inject.Listener;
import io.pinger.plus.storage.credentials.StorageConfig;
import io.pinger.plus.storage.implementation.StorageImplementation;
import io.pinger.plus.storage.type.StorageType;
import org.junit.jupiter.api.Test;

public class StorageModuleTest {

    @Test
    public void testStorageModule() {
        final Injector injector = Guice.createInjector(new TestModule());
        final SqlStorage storage = injector.getInstance(SqlStorage.class);
        storage.sayHello();
    }

    @RuntimeInitialized
    public static class SqlStorage implements Storage {
        private final AbstractStorageImpl storage;

        @Inject
        public SqlStorage(AbstractStorageImpl storage) {
            this.storage = storage;
        }

        @Override
        public StorageImplementation getImplementation() {
            return this.storage;
        }

        public void sayHello() {
            System.out.println("Saying hello");
            this.storage.hello();
            System.out.println("Finished");
        }
    }

    public static class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            this.install(new StorageModule<>(new StorageFactory(null), StorageType.MYSQL, AbstractStorageImpl.class, SqlStorage.class));
            this.bindListener(ClassMatcher.annotation(RuntimeInitialized.class), Object.class, instance -> System.out.println("WTFF"));
        }

        @SuppressWarnings("unchecked")
        protected <T> void bindListener(ClassMatcher matcher, Class<T> classifier, Listener<? super T> listener) {
            this.bindListener(matcher, new TypeListener() {
                @Override
                public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                    if (!classifier.isAssignableFrom(type.getRawType())) {
                        return;
                    }

                    encounter.register((Listener<? super I>) listener);
                }
            });
        }

    }

    public static class StorageFactory extends AbstractStorageFactory {
        public StorageFactory(StorageConfig config) {
            super(config);
        }

        @Override
        public StorageImplementation createStorage(StorageType type) {
            return new DubbyStorage();
        }
    }

    public abstract static class AbstractStorageImpl implements StorageImplementation {

        public abstract void hello();

    }

    public static class DubbyStorage extends AbstractStorageImpl {

        @Override
        public void hello() {
            System.out.println("hello 123");
        }

        @Override
        public void init() {
            System.out.println("init");
        }

        @Override
        public void shutdown() {
            System.out.println("shutdown");
        }
    }

}
