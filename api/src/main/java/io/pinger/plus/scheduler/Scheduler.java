package io.pinger.plus.scheduler;

public interface Scheduler {

    Task execute(Runnable runnable);

    default void run(Runnable runnable) {
        this.execute(runnable);
    }

    Task runLater(Runnable runnable, long delay);

    Task runRepeating(Runnable runnable, long delay, long interval);

}
