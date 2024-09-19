package io.pinger.plus.bungee.scheduler;

import io.pinger.plus.scheduler.Scheduler;
import io.pinger.plus.scheduler.Task;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class BungeeScheduler implements Scheduler {
    private final Plugin plugin;
    private final TaskScheduler scheduler;

    public BungeeScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getProxy().getScheduler();
    }

    @Override
    public Task execute(Runnable runnable) {
        final ScheduledTask task = this.scheduler.runAsync(this.plugin, runnable);
        return task::cancel;
    }


    @Override
    public Task runLater(Runnable runnable, long delay) {
        final ScheduledTask task = this.scheduler.schedule(this.plugin, runnable, delay, TimeUnit.MILLISECONDS);
        return task::cancel;
    }

    @Override
    public Task runRepeating(Runnable runnable, long delay, long interval) {
        final ScheduledTask task = this.scheduler.schedule(this.plugin, runnable, delay, interval, TimeUnit.MILLISECONDS);
        return task::cancel;
    }
}
