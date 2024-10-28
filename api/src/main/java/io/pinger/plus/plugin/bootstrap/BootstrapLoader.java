package io.pinger.plus.plugin.bootstrap;

import io.pinger.plus.classpath.ClassPath;
import io.pinger.plus.plugin.loader.LoadingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

public class BootstrapLoader {

    public <T> LoaderBootstrap loadBootstrap(Class<T> loaderClass, T plugin) {
        final Class<? extends LoaderBootstrap> bootstrapClass = this.loadBootstrapClass(plugin);
        if (bootstrapClass == null) {
            throw new LoadingException("Failed to find a bootstrap class for this plugin!", e);
        }

        final Constructor<? extends LoaderBootstrap> constructor;
        try {
            constructor = bootstrapClass.getConstructor(loaderClass);
        } catch (Exception e) {
            throw new LoadingException("Failed to find a bootstrap constructor!", e);
        }

        try {
            return constructor.newInstance(plugin);
        } catch (Exception e) {
            throw new LoadingException("Failed to instantiate bootstrap!", e);
        }
    }

    private <T> Class<? extends LoaderBootstrap> loadBootstrapClass(T plugin) {
        final ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());
        final List<Class<? extends LoaderBootstrap>> bootstraps = classPath.getSubTypesOf(LoaderBootstrap.class);
        return this.loadConcreteClass(bootstraps);
    }

    private Class<? extends LoaderBootstrap> loadConcreteClass(List<Class<? extends LoaderBootstrap>> bootstraps) {
        for (final Class<? extends LoaderBootstrap> bootstrap : bootstraps) {
            if (bootstrap.isInterface() || Modifier.isAbstract(bootstrap.getModifiers())) {
                continue;
            }

            return bootstrap;
        }

        return null;
    }

}
