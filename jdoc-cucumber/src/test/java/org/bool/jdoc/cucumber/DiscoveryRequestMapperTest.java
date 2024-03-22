package org.bool.jdoc.cucumber;

import org.bool.jdoc.core.DiscoveryRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryListener;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DiscoveryRequestMapperTest {

    @Mock
    private FeatureLoader featureLoader;

    @InjectMocks
    private DiscoveryRequestMapper requestMapper;

    @Test
    void test(@Mock DiscoverySelector selector, @Mock DiscoveryFilter<?> filter, @Mock EngineDiscoveryListener listener,
            @Mock ConfigurationParameters params) {
        var requestIn = DiscoveryRequest.builder()
                .selectors(List.of(selector))
                .filters(List.of(filter))
                .listener(listener)
                .params(params)
                .build();
        given(featureLoader.loadFeatures(requestIn))
            .willReturn(List.of(Path.of("build.gradle")));

        assertThat(requestMapper.toCucumberDiscoveryRequest(requestIn))
            .returns(List.of(DiscoverySelectors.selectFile(Path.of("build.gradle").toFile())), dr -> dr.getSelectorsByType(DiscoverySelector.class))
            .returns(List.of(filter), dr -> dr.getFiltersByType(DiscoveryFilter.class))
            .returns(listener, EngineDiscoveryRequest::getDiscoveryListener)
            .returns(params, EngineDiscoveryRequest::getConfigurationParameters)
            ;
    }
}
