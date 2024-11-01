package io.pinger.plus.velocity.dependency;

import io.pinger.plus.dependency.AbstractDependencyManager;
import net.byteflux.libby.VelocityLibraryManager;

public class SimpleDependencyManager extends AbstractDependencyManager {

    public SimpleDependencyManager(VelocityLibraryManager manager) {
        super(manager);
    }
}
