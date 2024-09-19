package io.pinger.plus.spigot.scheduler;

import io.pinger.plus.scheduler.Scheduler;
import io.pinger.plus.scheduler.Task;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class SpigotSyncScheduler implements Scheduler {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public SpigotSyncScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = this.plugin.getServer().getScheduler();
    }

    @Override
    public Task execute(Runnable runnable) {
        final BukkitTask task = this.scheduler.runTask(this.plugin, runnable);
        return task::cancel;
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        final BukkitTask task = this.scheduler.runTaskLater(this.plugin, runnable, delay);
        return task::cancel;
    }

    @Override
    public Task runRepeating(Runnable runnable, long delay, long interval) {
        final BukkitTask task = this.scheduler.runTaskTimer(this.plugin, runnable, delay, interval);
        return task::cancel;
    }
}
