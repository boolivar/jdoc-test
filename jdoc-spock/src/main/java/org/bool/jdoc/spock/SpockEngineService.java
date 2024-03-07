package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.UniqueId;
import org.spockframework.runtime.SpockEngine;

@AllArgsConstructor
public class SpockEngineService {

    public JdocSpockEngineDescriptor discover(SpockEngine spockEngine, EngineDiscoveryRequest request, UniqueId uniqueId) {
        return JdocSpockEngineDescriptor.builder().build();
    }
}
