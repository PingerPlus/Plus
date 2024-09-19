package io.pinger.plus.scheduler;

public class AsyncScheduler implements Scheduler {
    @Override
    public Task execute(Runnable runnable) {
        return null;
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return null;
    }

    @Override
    public Task runRepeating(Runnable runnable, long delay, long interval) {
        return null;
    }
}
