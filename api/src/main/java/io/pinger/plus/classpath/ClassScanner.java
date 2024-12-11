package io.pinger.plus.classpath;

import io.pinger.plus.Bootstrap;
import io.pinger.plus.asm.ClassProxy;
import io.pinger.plus.graph.Graph;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ClassScanner {
    private final Graph<ClassProxy> graph;

    public ClassScanner(ClassLoader loader) {
        this.graph = InheritanceGraph.from(loader).getGraph();
    }

    public ClassScanner(Bootstrap bootstrap) {
        this(bootstrap.getClass().getClassLoader());
    }

    public <T> List<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return this.streamGraph(this.graph, type)
                .map(ClassProxy::load)
                .map((clazz) -> (Class<? extends T>) clazz.asSubclass(type))
                .collect(Collectors.toList());
    }

    public List<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return this.streamGraph(this.graph, Object.class)
                .filter(classProxy -> classProxy.hasAnnotation(annotation))
                .map(ClassProxy::load)
                .collect(Collectors.toList());
    }

    public Graph<ClassProxy> getGraph() {
        return this.graph;
    }

    private Stream<ClassProxy> streamGraph(Graph<ClassProxy> graph, Class<?> clazz) {
        return this.streamIterable(graph.traverse(ClassProxy.fromClass(clazz)));
    }

    private Stream<ClassProxy> streamIterable(Iterable<ClassProxy> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
