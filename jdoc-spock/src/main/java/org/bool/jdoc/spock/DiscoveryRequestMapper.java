package org.bool.jdoc.spock;

import org.junit.platform.engine.EngineDiscoveryRequest;

import java.util.List;

public class DiscoveryRequestMapper {

    public EngineDiscoveryRequest toSpockDiscoveryRequest(EngineDiscoveryRequest request, SpecClassMapper specClassMapper) {
        return DiscoveryRequest.builder().params(request.getConfigurationParameters()).selectors(List.of()).build();
    }
}
