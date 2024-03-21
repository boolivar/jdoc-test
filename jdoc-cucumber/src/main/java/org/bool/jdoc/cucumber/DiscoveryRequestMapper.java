package org.bool.jdoc.cucumber;

import org.bool.jdoc.core.DiscoveryRequest;

import lombok.AllArgsConstructor;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DiscoveryRequestMapper {

    private final FeatureLoader featureLoader;

    public DiscoveryRequestMapper() {
        this(new FeatureLoader());
    }

    public EngineDiscoveryRequest toCucumberDiscoveryRequest(EngineDiscoveryRequest discoveryRequest) {
        List<Path> features = featureLoader.loadFeatures(discoveryRequest);
        return DiscoveryRequest.builder()
                .selectors(features.stream().map(Path::toFile).map(DiscoverySelectors::selectFile).collect(Collectors.toList()))
                .filters(discoveryRequest.getFiltersByType(DiscoveryFilter.class))
                .params(discoveryRequest.getConfigurationParameters())
                .listener(discoveryRequest.getDiscoveryListener())
                .build();
    }
}
