package org.bool.jdoc.cucumber;

import org.bool.jdoc.core.DiscoveryRequest;

import lombok.AllArgsConstructor;
import org.junit.platform.engine.EngineDiscoveryRequest;

@AllArgsConstructor
public class DiscoveryRequestMapper {

    public EngineDiscoveryRequest toCucumberDiscoveryRequest(EngineDiscoveryRequest discoveryRequest) {
        return DiscoveryRequest.builder().build();
    }
}
