package io.pinger.plus.asm;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassProxy implements Comparable<ClassProxy> {
    private final List<String> annotationClasses = new ArrayList<>();
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

    public void addAnnotation(String annotation) {
        this.annotationClasses.add(annotation);
    }

    public boolean hasAnnotation(String annotation) {
        return this.annotationClasses.contains(annotation);
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        final String parsedAnnotation = annotation.getName().replace(".", "/");
        return this.annotationClasses.contains(parsedAnnotation);
    }

    public List<String> getAnnotationClasses() {
        return this.annotationClasses;
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

    @SneakyThrows
    public Class<?> load() {
        return Class.forName(this.getJavaClassName());
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
