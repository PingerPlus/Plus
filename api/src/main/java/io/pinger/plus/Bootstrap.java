package io.pinger.plus;

import com.google.inject.Injector;
import io.pinger.plus.asm.ClassProxy;
import io.pinger.plus.classpath.ClassScanner;
import io.pinger.plus.graph.Graph;
import io.pinger.plus.platform.Platform;
import io.pinger.plus.plugin.logging.PluginLogger;
import java.io.InputStream;
import java.nio.file.Path;

public interface Bootstrap {

    PlusPlugin getPlugin();

    PluginLogger getLogger();

    PluginModule getModule();

    Injector getInjector();

    ClassScanner getClassScanner();

    default Graph<ClassProxy> getGraph() {
        return this.getClassScanner().getGraph();
    }

    Platform getPlatform();

    Path getDataDirectory();

    default InputStream getResource(String resourceName) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceName);
    }

    default <T> T get(Class<T> clazz) {
        return this.getInjector().getInstance(clazz);
    }

}
