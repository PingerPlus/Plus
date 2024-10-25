package io.pinger.plus.event;

import io.pinger.plus.subscribe.Subscribable;

public interface RegisteredEvent extends Subscribable {

    void unregister();

}
