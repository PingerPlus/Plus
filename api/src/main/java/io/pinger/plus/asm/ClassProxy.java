package io.pinger.plus.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class ClassProxy implements Comparable<ClassProxy> {
    private final String className;

    private int access;

    public ClassProxy(String className, int access) {
        this.className = className;
        this.access = access;
    }

    public static ClassProxy fromClass(Class<?> clazz) {
        final String className = clazz.getName().replace(".", "/");
        return new ClassProxy(className, clazz.getModifiers());
    }

    public String getClassName() {
        return this.className;
    }

    public String getJavaClassName() {
        return this.className.replace("/", ".");
    }

    public int getAccess() {
        return this.access;
    }

    public boolean isInterface() {
        return (this.access & Opcodes.ACC_INTERFACE) != 0;
    }

    public boolean isAbstract() {
        return (this.access & Opcodes.ACC_ABSTRACT) != 0;
    }

    public boolean isConcreteClass() {
        return !this.isAbstract() && !this.isInterface();
    }

    public void mergeAccess(int newAccess) {
        this.access |= newAccess; // Merge access flags (bitwise OR)
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        final ClassProxy proxy = (ClassProxy) object;
        return Objects.equals(this.className, proxy.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.className, this.access);
    }

    @Override
    public int compareTo(@NotNull ClassProxy o) {
        return this.className.compareTo(o.className);
    }
}
