package io.pinger.plus.annotation;

import java.lang.annotation.Annotation;

public interface Annotations {

    static boolean presentDeep(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.getAnnotation(annotationClass) != null) {
            return true;
        }

        for (final Annotation annotation : clazz.getAnnotations()) {
            final Class<? extends Annotation> type = annotation.annotationType();
            return Annotations.presentDeep(type, annotationClass);
        }

        return false;
    }

    static boolean present(Class<?> clazz, Class<? extends Annotation>... annotations) {
        for (final Class<? extends Annotation> annotation : annotations) {
            if (!clazz.isAnnotationPresent(annotation)) {
                return false;
            }
        }

        return true;
    }

}
