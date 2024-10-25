package io.pinger.plus.inject;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;

public class MultibinderWrapper<T> {
    private final Multibinder<T> multibinder;

    public MultibinderWrapper(Binder binder, Class<T> type) {
        this.multibinder = Multibinder.newSetBinder(binder, type);
    }

    public void addBinding(Class<? extends T> type) {
        this.multibinder.addBinding().to(type);
    }

    public void addBinding(T instance) {
        this.multibinder.addBinding().toInstance(instance);
    }
}
