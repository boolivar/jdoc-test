package org.bool.jdoc.spock;

import lombok.Builder;
import lombok.NonNull;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class JdocSpockEngineDescriptor extends EngineDescriptor implements AutoCloseable {

    private final AutoCloseable resource;

    @Builder
    public JdocSpockEngineDescriptor(@NonNull UniqueId uniqueId, @NonNull String displayName, AutoCloseable resource) {
        super(uniqueId, displayName);
        this.resource = resource;
    }

    @Override
    public void close() throws Exception {
        if (resource != null) {
            resource.close();
        }
    }
}
