package org.bool.jdoc.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryListener;
import org.junit.platform.engine.EngineDiscoveryRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class DiscoveryRequest implements EngineDiscoveryRequest {

    @NonNull
    private final ConfigurationParameters params;

    @NonNull
    private final List<DiscoverySelector> selectors;

    @NonNull
    @Builder.Default
    private final List<? super DiscoveryFilter<?>> filters = Collections.emptyList();

    @NonNull
    @Builder.Default
    private final EngineDiscoveryListener listener = EngineDiscoveryListener.NOOP;

    @Override
    public ConfigurationParameters getConfigurationParameters() {
        return params;
    }

    @Override
    public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType) {
        return selectors.stream().filter(selectorType::isInstance).map(selectorType::cast).collect(Collectors.toList());
    }

    @Override
    public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
        return filters.stream().filter(filterType::isInstance).map(filterType::cast).collect(Collectors.toList());
    }

    @Override
    public EngineDiscoveryListener getDiscoveryListener() {
        return listener;
    }
}
