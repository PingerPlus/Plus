package io.pinger.plus.event;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for interacting with the event system.
 *
 * <p>The {@code Events} class provides static methods to simplify firing events and registering listeners
 * across the system. It acts as a high-level interface to the {@link EventRegistry} and its associated
 * {@link EventManager} instances.</p>
 */
public interface Events {

    /**
     * Fires the given event by dispatching it to the appropriate {@link EventManager}.
     *
     * <p>This method locates the event manager responsible for handling the event's type and invokes
     * its {@link EventManager#fire(Object)} method to notify all registered listeners.</p>
     *
     * @param event the event to fire
     */
    static void call(@NotNull Object event) {
        EventRegistry.get()
            .findEventManagerFor(event)
            .orElseThrow(() -> new IllegalStateException("Failed to find an event manager for " + event.getClass().getSimpleName()))
            .fire(event);
    }

    /**
     * Registers a listener for a specific event type with the given priority.
     *
     * @param eventClass the class of the event to listen for
     * @param priority   the priority of the listener
     * @param listener   the function that handles the event
     * @param <T>        the type of the event
     * @return a {@link RegisteredEvent} that can be used to unregister the listener
     */
    static <T> RegisteredEvent listen(Class<T> eventClass, Priority priority, Consumer<T> listener) {
        return EventRegistry.get()
            .findEventManagerFor(eventClass)
            .orElseThrow(() -> new IllegalStateException("Failed to find an event manager for " + eventClass.getSimpleName()))
            .listen(eventClass, priority, listener);
    }

    /**
     * Registers a listener for a specific event type with the default priority {@link Priority#NORMAL}.
     *
     * <p>This is a convenience method for registering listeners when the default priority is sufficient.</p>
     *
     * @param eventClass the class of the event to listen for
     * @param listener   the function that handles the event
     * @param <T>        the type of the event
     * @return a {@link RegisteredEvent} that can be used to unregister the listener
     */
    static <T> RegisteredEvent listen(Class<T> eventClass, Consumer<T> listener) {
        return Events.listen(eventClass, Priority.NORMAL, listener);
    }

}
