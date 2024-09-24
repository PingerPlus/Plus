package io.pinger.plus.event;

import java.util.function.Consumer;

public abstract class EventManager<E> {
    private final Class<E> rootEventClass;

    protected EventManager(Class<E> rootEventClass) {
        this.rootEventClass = rootEventClass;
    }

    public boolean accepts(Class<?> eventClass) {
        return this.rootEventClass.isAssignableFrom(eventClass);
    }

    /**
     * Calls this event, which in result invokes all listeners, or {@link RegisteredEvent events}
     * that are currently listening to this event.
     *
     * @param event the event to fire
     */
    public abstract void fire(E event);

    /**
     * Registers a listener for a specific event class with a given priority.
     *
     * <p>The listener will be invoked when the event which is being referenced by the class
     * is fired. The execution order of listeners is determined by their priority.</p>
     *
     * @param eventClass the class of the event to listen for
     * @param priority   the priority of the listener
     * @param listener   the function that handles the event
     * @param <T>        the type of the event
     * @return a {@link RegisteredEvent} that can be used to unregister the listener
     */
    public abstract <T extends E> RegisteredEvent listen(Class<T> eventClass, Priority priority, Consumer<T> listener);

    /**
     * Registers a listener for a specific event class with the default priority {@link Priority#NORMAL}.
     *
     * <p>This is a convenience method for registering listeners when the default priority is sufficient.</p>
     *
     * @param eventClass the class of the event to listen for
     * @param listener   the function that handles the event
     * @param <T>        the type of the event
     * @return a {@link RegisteredEvent} that can be used to unregister the listener
     */
    public <T extends E> RegisteredEvent listen(Class<T> eventClass, Consumer<T> listener) {
        return this.listen(eventClass, Priority.NORMAL, listener);
    }

}
