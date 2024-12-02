package io.pinger.plus.plugin.bootstrap;

import io.pinger.plus.asm.ClassProxy;
import io.pinger.plus.classpath.InheritanceGraph;
import io.pinger.plus.graph.Graph;
import io.pinger.plus.plugin.loader.LoadingException;
import io.pinger.plus.util.Iterables;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BootstrapLoader {

    public <T> LoaderBootstrap loadBootstrap(Class<T> loaderClass, T plugin) {
        final Class<? extends LoaderBootstrap> bootstrapClass = this.loadBootstrapClass(plugin);
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
        final InheritanceGraph scanner = InheritanceGraph.from(plugin.getClass().getClassLoader());
        final Graph<ClassProxy> graph = scanner.getGraph();
        final ClassProxy loaderBootstrap = ClassProxy.fromClass(LoaderBootstrap.class);
        return this.findConcreteImplementation(graph.traverse(loaderBootstrap));
    }

    private Class<? extends LoaderBootstrap> findConcreteImplementation(Iterable<ClassProxy> traversal) {
        final List<ClassProxy> proxies = StreamSupport.stream(traversal.spliterator(), false)
                .filter(ClassProxy::isConcreteClass)
                .collect(Collectors.toList());

        if (proxies.isEmpty()) {
            throw new LoadingException("Failed to find a proxy which implements LoaderBoostrap");
        }

        if (proxies.size() > 1) {
            throw new LoadingException("Couldn't decide which LoaderBootstrap to use as there is more than 1...");
        }

        final String className = Iterables.queryFirst(proxies, ClassProxy::getJavaClassName);
        System.out.println(className);
        try {
            return Class.forName(className).asSubclass(LoaderBootstrap.class);
        } catch (Exception e) {
            throw new LoadingException("Failed to find a bootstrap class for this plugin");
        }
    }
}
