package io.pinger.plus.scheduler;

import io.pinger.plus.subscribe.Subscribable;

public interface Task extends Subscribable {

    void cancel();

}
