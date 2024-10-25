package io.pinger.plus.inject;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import java.lang.annotation.Annotation;

public interface ClassMatcher extends Matcher<TypeLiteral<?>> {

    static ClassMatcher annotation(Class<? extends Annotation> annotation) {
        return (classifier) -> classifier.isAnnotationPresent(annotation);
    }

    boolean matches(Class<?> classifier);

    @Override
    default boolean matches(TypeLiteral<?> literal) {
        return this.matches(literal.getRawType());
    }
}
