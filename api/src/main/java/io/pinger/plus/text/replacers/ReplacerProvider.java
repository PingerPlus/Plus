package io.pinger.plus.text.replacers;

import io.pinger.plus.text.Replacer;

public interface ReplacerProvider<T> {

    boolean provides(Class<?> clazz);

    T provide(T object, Replacer replacer);

}
