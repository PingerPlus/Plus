package io.pinger.plus.velocity.scheduler;

import com.google.inject.Inject;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler.TaskBuilder;
import io.pinger.plus.scheduler.Scheduler;
import io.pinger.plus.scheduler.Task;
import io.pinger.plus.velocity.PluginBootstrap;
import java.util.concurrent.TimeUnit;

public class VelocityScheduler implements Scheduler {
    private final PluginBootstrap bootstrap;

    @Inject
    public VelocityScheduler(PluginBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public Task execute(Runnable runnable) {
        final ScheduledTask task = this.buildTask(runnable).schedule();
        return task::cancel;
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        final ScheduledTask task = this.buildTask(runnable).delay(delay, TimeUnit.MILLISECONDS).schedule();
        return task::cancel;
    }

    @Override
    public Task runRepeating(Runnable runnable, long delay, long interval) {
        final ScheduledTask task = this.buildTask(runnable)
            .delay(delay, TimeUnit.MILLISECONDS)
            .repeat(interval, TimeUnit.MILLISECONDS)
            .schedule();

        return task::cancel;
    }

    private TaskBuilder buildTask(Runnable runnable) {
        return this.bootstrap.getProxyServer().getScheduler().buildTask(this.bootstrap, runnable);
    }
}
