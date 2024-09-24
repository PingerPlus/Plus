package io.pinger.plus.event;

import io.pinger.plus.instance.Instances;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * A central registry that holds and manages multiple {@link EventManager} instances.
 *
 * <p>The {@code EventRegistry} allows the registration, removal, and lookup of {@link EventManager}
 * instances, facilitating the firing and handling of events within the system. It ensures that the
 * appropriate {@code EventManager} is found for a given event type and that events are dispatched correctly.</p>
 */
public class EventRegistry {
    private final Set<EventManager<?>> eventManagers = new HashSet<>();

    public EventRegistry() {
    }

    public static EventRegistry get() {
        return Instances.getOrThrow(EventRegistry.class);
    }

    /**
     * Finds an {@link EventManager} that can handle events of the specified class.
     *
     * <p>The method checks each registered {@code EventManager} to find one that
     * accepts the given event class. If no suitable manager is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param clazz the class of the event
     * @param <T>   the type of the event
     * @return an {@link Optional} containing the {@link EventManager} if found, otherwise empty
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<EventManager<T>> findEventManagerFor(Class<T> clazz) {
        for (final EventManager<?> eventManager : this.eventManagers) {
            if (eventManager.accepts(clazz)) {
                return Optional.of((EventManager<T>) eventManager);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds an {@link EventManager} for a specific event instance.
     *
     * <p>This method performs the same operation as {@link #findEventManagerFor(Class)}, but for a
     * concrete event instance, inferring the event type from the object's class.</p>
     *
     * @param object the event instance
     * @param <T>    the type of the event
     * @return an {@link Optional} containing the {@link EventManager} if found, otherwise empty
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<EventManager<T>> findEventManagerFor(@NotNull T object) {
        return this.findEventManagerFor((Class<T>) object.getClass());
    }

    /**
     * Registers one or more {@link EventManager} instances to the registry.
     *
     * <p>Once registered, the managers will handle events of their respective types.
     * This allows the event system to delegate event handling to the appropriate manager.</p>
     *
     * @param eventManagers the event managers to register
     */
    public void registerEventManager(EventManager<?>... eventManagers) {
        this.eventManagers.addAll(Arrays.asList(eventManagers));
    }

    /**
     * Removes an {@link EventManager} from the registry.
     *
     * <p>Once removed, the manager will no longer handle events, and its listeners will not be invoked.</p>
     *
     * @param eventManager the event manager to remove
     */
    public void removeEventManager(EventManager<?> eventManager) {
        this.eventManagers.remove(eventManager);
    }
}
