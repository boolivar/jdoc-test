package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import lombok.Builder;
import lombok.NonNull;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.io.Closeable;
import java.io.IOException;

public class JdocSpockEngineDescriptor extends EngineDescriptor implements Closeable {

    private final GroovyClassLoader classLoader;

    @Builder
    public JdocSpockEngineDescriptor(@NonNull UniqueId uniqueId, @NonNull String displayName, GroovyClassLoader classLoader) {
        super(uniqueId, displayName);
        this.classLoader = classLoader;
    }

    @Override
    public void close() throws IOException {
        if (classLoader != null) {
            classLoader.close();
        }
    }
}
