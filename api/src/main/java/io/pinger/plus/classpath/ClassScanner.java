package io.pinger.plus.classpath;

import com.google.inject.Inject;
import io.pinger.plus.Bootstrap;
import io.pinger.plus.classpath.ClassPath.ClassInfo;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassScanner {
    private final ClassPath classPath;

    @Inject
    public ClassScanner(Bootstrap bootstrap) {
        this.classPath = ClassPath.from(bootstrap.getClass().getClassLoader());
    }

    public Set<Class<?>> getAllClasses() {
        return this.classPath.getAllClasses()
            .stream()
            .map(ClassInfo::load)
            .collect(Collectors.toSet());
    }

    @SafeVarargs
    public final Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotation) {
        return this.classPath.findClassesAnnotatedWith(annotation);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return this.getAllClasses().stream()
            .filter(type::isAssignableFrom)
            .map((clazz) -> (Class<? extends T>) clazz.asSubclass(type))
            .collect(Collectors.toSet());
    }

}
