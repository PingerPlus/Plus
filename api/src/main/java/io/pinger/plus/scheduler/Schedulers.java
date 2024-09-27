package io.pinger.plus.scheduler;

import io.pinger.plus.instance.Instances;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Schedulers {
    private final Scheduler sync;
    private final Scheduler async;

    public static final ScheduledExecutorService DEFAULT_EXECUTOR = Executors.newScheduledThreadPool(2, (r) -> {
        final Thread thread = Executors.defaultThreadFactory().newThread(r);
        thread.setName("plus-scheduler");
        return thread;
    });

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
