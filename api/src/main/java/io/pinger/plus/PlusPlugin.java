package io.pinger.plus;

import io.pinger.plus.plugin.logging.PluginLogger;

public interface PlusPlugin {

    Bootstrap getBootstrap();

    PluginLogger getLogger();
}
