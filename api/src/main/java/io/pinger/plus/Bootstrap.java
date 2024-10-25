package io.pinger.plus;

import io.pinger.plus.plugin.logging.PluginLogger;

public interface Bootstrap {

    PluginLogger getLogger();

    PluginModule getModule();

}
