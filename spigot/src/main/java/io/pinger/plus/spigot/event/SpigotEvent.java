package io.pinger.plus.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public interface SpigotEvent<T extends Event> extends Listener, EventExecutor {

    void handle(T event);

    @Override
    @SuppressWarnings("unchecked")
    default void execute(@NotNull Listener listener, @NotNull Event event) {
        this.handle((T) event);
    }
}
