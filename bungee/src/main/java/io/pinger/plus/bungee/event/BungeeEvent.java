package io.pinger.plus.bungee.event;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;

public interface BungeeEvent<T extends Event> extends Listener {

    void handle(T event);

}
