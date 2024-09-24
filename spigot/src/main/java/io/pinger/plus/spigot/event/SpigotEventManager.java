package io.pinger.plus.spigot.event;

import io.pinger.plus.event.EventManager;
import io.pinger.plus.event.Priority;
import io.pinger.plus.event.RegisteredEvent;
import io.pinger.plus.instance.Instances;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class SpigotEventManager extends EventManager<Event> {

    public SpigotEventManager() {
        super(Event.class);
    }

    @Override
    public void fire(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public <T extends Event> RegisteredEvent listen(Class<T> eventClass, Priority priority, Consumer<T> listener) {
        final SpigotEvent<T> spigotEvent = (event) -> listener.accept(eventClass.cast(event));
        final RegisteredEvent registeredEvent = () -> HandlerList.unregisterAll(spigotEvent);

        Bukkit.getPluginManager().registerEvent(
            eventClass,
            spigotEvent,
            this.priorityToEventPriority(priority),
            spigotEvent,
            Instances.get(Plugin.class)
        );

        return registeredEvent;
    }

    private EventPriority priorityToEventPriority(Priority priority) {
        switch (priority) {
            case LOWEST: return EventPriority.LOWEST;
            case LOW: return EventPriority.LOW;
            case NORMAL: return EventPriority.NORMAL;
            case HIGH: return EventPriority.HIGH;
            case HIGHEST: return EventPriority.HIGHEST;
            case MONITOR: return EventPriority.MONITOR;
            default: throw new IllegalStateException("Unknown priority: " + priority);
        }
    }
}
