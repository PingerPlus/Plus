package io.pinger.plus.velocity.event;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.proxy.ProxyServer;
import io.pinger.plus.event.EventManager;
import io.pinger.plus.event.Priority;
import io.pinger.plus.event.RegisteredEvent;
import io.pinger.plus.instance.Instances;
import java.util.function.Consumer;

public class VelocityEventManager extends EventManager<Object> {
    private final ProxyServer server;

    public VelocityEventManager() {
        super(Object.class);

        // TODO: Change this to velocity server
        this.server = Instances.get(ProxyServer.class);
    }

    @Override
    public void fire(Object event) {
        this.server.getEventManager().fireAndForget(event);
    }

    @Override
    public <T> RegisteredEvent listen(Class<T> eventClass, Priority priority, Consumer<T> listener) {
        final EventHandler<T> handler = listener::accept;
        final RegisteredEvent event = () -> this.server.getEventManager().unregister(this.server, handler);

        this.server.getEventManager().register(
            this.server,
            eventClass,
            this.priorityToByte(priority),
            handler
        );

        return event;
    }

    private byte priorityToByte(Priority priority) {
        return switch (priority) {
            case LOWEST -> -64;
            case LOW -> -32;
            case NORMAL -> 0;
            case HIGH -> 32;
            case HIGHEST -> 64;
            default -> throw new IllegalStateException("Unknown priority: " + priority);
        };
    }
}
