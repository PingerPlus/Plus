package io.pinger.plus.scheduler;

import io.pinger.plus.instance.Instances;

public class Schedulers {
    private final Scheduler sync;
    private final Scheduler async;

    public Schedulers(Scheduler sync, Scheduler async) {
        this.sync = sync;
        this.async = async;
    }

    public static Scheduler sync() {
        return Instances.getOrThrow(Schedulers.class).sync;
    }

    public static Scheduler async() {
        return Instances.getOrThrow(Schedulers.class).async;
    }
}
