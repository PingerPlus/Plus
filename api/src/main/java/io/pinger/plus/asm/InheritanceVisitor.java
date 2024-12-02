package io.pinger.plus.asm;

import io.pinger.plus.graph.Graph;
import io.pinger.plus.util.Iterables;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class InheritanceVisitor extends ClassVisitor {
    private final Graph<ClassProxy> graph;

    public InheritanceVisitor(Graph<ClassProxy> graph) {
        super(Opcodes.ASM9);

        this.graph = graph;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ClassProxy proxy = this.queryForClass(name);
        if (proxy == null) {
            proxy = new ClassProxy(name, access);
            this.graph.addNode(proxy);
        } else {
            proxy.mergeAccess(access);
        }

        if (superName != null) {
            ClassProxy superProxy = this.queryForClass(superName);
            if (superProxy == null) {
                superProxy = new ClassProxy(superName, 0);
            }

            this.graph.addEdge(superProxy, proxy);
        }

        for (final String interfaceName : interfaces) {
            ClassProxy interfaceProxy = this.queryForClass(interfaceName);
            if (interfaceProxy == null) {
                interfaceProxy = new ClassProxy(interfaceName, 0);
            }

            this.graph.addEdge(interfaceProxy, proxy);
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    private ClassProxy queryForClass(String className) {
        return Iterables.queryFirst(this.graph.getVertices(), ClassProxy::getClassName, className);
    }
}
