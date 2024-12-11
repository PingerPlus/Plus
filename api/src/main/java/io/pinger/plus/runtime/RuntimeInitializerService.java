package io.pinger.plus.runtime;

import com.google.inject.Inject;
import io.pinger.plus.classpath.ClassScanner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RuntimeInitializerService {
    private final ClassScanner scanner;
    private final Set<ClassInitializer> initializers;

    @Inject
    public RuntimeInitializerService(ClassScanner scanner) {
        this.scanner = scanner;
        this.initializers = new HashSet<>();
    }

    public void init() {
        final List<Class<?>> allClasses = this.scanner.getSubTypesOf(Object.class);
        for (final ClassInitializer initializer : this.initializers) {
            for (final Class<?> clazz : allClasses) {
                if (initializer.accepts(clazz)) {

                }
            }
        }
    }
}
