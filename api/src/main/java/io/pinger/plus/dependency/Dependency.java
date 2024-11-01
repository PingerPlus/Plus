package io.pinger.plus.dependency;

import java.util.HashSet;
import java.util.Set;
import net.byteflux.libby.Library;
import net.byteflux.libby.relocation.Relocation;

public interface Dependency {

    String groupId();

    String artifactId();

    String version();

    default Set<Relocation> relocations() {
        return new HashSet<>();
    }

    default Library toLibrary() {
        final Library.Builder builder = new Library.Builder()
            .groupId(this.groupId())
            .artifactId(this.artifactId())
            .version(this.version());

        if (!this.relocations().isEmpty()) {
            this.relocations().forEach(builder::relocate);
        }

        return builder.build();
    }

}
