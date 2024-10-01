package io.pinger.plus.bungee.event;

import io.pinger.plus.event.EventManager;
import io.pinger.plus.event.Priority;
import io.pinger.plus.event.RegisteredEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventBus;
import net.md_5.bungee.event.EventPriority;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BungeeEventManager extends EventManager<Event> {

    public BungeeEventManager() {
        super(Event.class);
    }

    @Override
    public void fire(Event event) {
        ProxyServer.getInstance().getPluginManager().callEvent(event);
    }

    @Override
    public <E extends Event> RegisteredEvent listen(Class<E> eventClass, Priority priority, Consumer<E> listener) {
        return this.registerEvent(listener::accept, eventClass, priority);
    }

    private byte priorityToEventPriority(Priority priority) {
        switch (priority) {
            case LOWEST: return EventPriority.LOWEST;
            case LOW: return EventPriority.LOW;
            case NORMAL: return EventPriority.NORMAL;
            case HIGH: return EventPriority.HIGH;
            case HIGHEST: return EventPriority.HIGHEST;
            default: throw new IllegalArgumentException("Unknown priority: " + priority);
        }
    }

    @SneakyThrows
    private <E extends Event> RegisteredEvent registerEvent(BungeeEvent<E> event, Class<E> eventClass, Priority priority) {
        final PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        final Field field = PluginManager.class.getDeclaredField("eventBus");
        field.setAccessible(true);

        final byte eventPriority = this.priorityToEventPriority(priority);

        final EventBus eventBus = (EventBus) field.get(pluginManager);
        final RegisteredEvent registeredEvent = this.registerInternally(eventBus, event, eventClass, eventPriority);

        this.bakeHandlers(eventClass, eventBus);

        return registeredEvent;
    }

    @SneakyThrows
    private <E extends Event> RegisteredEvent registerInternally(EventBus eventBus, BungeeEvent<E> event, Class<E> eventClass, byte priority) throws Exception {
        final Field priorityField = EventBus.class.getDeclaredField("byListenerAndPriority");
        priorityField.setAccessible(true);

        final Map listenerMap = (Map) priorityField.get(eventBus);
        final Map byClass = (Map) listenerMap.computeIfAbsent(eventClass, ($) -> new HashMap<>());
        final Map byPriority = (Map) byClass.computeIfAbsent(priority, ($) -> new HashMap<>());

        final Method method = this.findEventHandler(event);
        byPriority.put(event, new Method[] { method });

        return () -> byPriority.remove(event);
    }

    @SneakyThrows
    private <E extends Event> void bakeHandlers(Class<E> eventClass, EventBus eventBus) {
        final Method method = EventBus.class.getDeclaredMethod("bakeHandlers", Class.class);
        method.setAccessible(true);
        method.invoke(eventBus, eventClass);
    }

    private <E extends Event> Method findEventHandler(BungeeEvent<E> event) {
        for (final Method method : event.getClass().getDeclaredMethods()) {
            final Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1 && parameters[0].equals(Event.class)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
}
