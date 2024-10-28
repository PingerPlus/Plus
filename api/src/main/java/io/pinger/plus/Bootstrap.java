package io.pinger.plus;

import com.google.inject.Injector;
import io.pinger.plus.platform.Platform;
import io.pinger.plus.plugin.logging.PluginLogger;
import java.io.InputStream;
import java.nio.file.Path;

public interface Bootstrap {

    PluginLogger getLogger();

    PluginModule getModule();

    Injector getInjector();

    Platform getPlatform();

    Path getDataDirectory();

    default InputStream getResource(String resourceName) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceName);
    }

}
