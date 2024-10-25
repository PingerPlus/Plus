package io.pinger.plus.annotation;

import java.lang.annotation.Annotation;

public interface Annotations {

    static boolean present(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.getAnnotation(annotationClass) != null) {
            return true;
        }

        for (final Annotation annotation : clazz.getAnnotations()) {
            final Class<? extends Annotation> type = annotation.annotationType();
            return Annotations.present(type, annotationClass);
        }

        return false;
    }

}
