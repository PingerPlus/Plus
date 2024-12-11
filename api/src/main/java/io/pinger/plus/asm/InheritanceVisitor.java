package io.pinger.plus.asm;

import io.pinger.plus.graph.Graph;
import io.pinger.plus.util.Iterables;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class InheritanceVisitor extends ClassVisitor {
    private final Graph<ClassProxy> graph;

    private ClassProxy currentClass;

    public InheritanceVisitor(Graph<ClassProxy> graph) {
        super(Opcodes.ASM9);

        this.graph = graph;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.currentClass = this.queryForClass(name);
        this.currentClass.mergeAccess(access);

        if (superName != null) {
            final ClassProxy superProxy = this.queryForClass(superName);
            this.graph.addEdge(superProxy, this.currentClass);
        }

        for (final String interfaceName : interfaces) {
            final ClassProxy interfaceProxy = this.queryForClass(interfaceName);
            this.graph.addEdge(interfaceProxy, this.currentClass);
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        final String annotationClassName = descriptor.substring(1, descriptor.length() - 1);
        this.currentClass.addAnnotation(annotationClassName);
        return super.visitAnnotation(descriptor, visible);
    }

    private ClassProxy queryForClass(String className) {
        final ClassProxy proxy = Iterables.queryFirst(this.graph.getVertices(), ClassProxy::getClassName, className);
        if (proxy == null) {
            return new ClassProxy(className, 0);
        }
        return proxy;
    }
}
