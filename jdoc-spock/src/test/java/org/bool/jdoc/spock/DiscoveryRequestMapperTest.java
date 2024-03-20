package org.bool.jdoc.spock;

import org.bool.jdoc.core.DiscoveryRequest;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DiscoveryRequestMapperTest {

    @TempDir
    private static Path tempDir;

    private final DiscoveryRequestMapper requestMapper = new DiscoveryRequestMapper();

    @Test
    void testMapping(@Mock ConfigurationParameters params, @Mock SpecClassMapper classMapper) {
        var file = tempDir.resolve("File.java").toString();
        var request = DiscoveryRequest.builder()
                .params(params).selectors(List.of(DiscoverySelectors.selectFile(file))).build();

        given(classMapper.toTestSpecClasses(Paths.get(file)))
                .willReturn(List.of(DiscoveryRequestMapper.class));

        assertThat(requestMapper.toSpockDiscoveryRequest(request, classMapper))
                .returns(params, EngineDiscoveryRequest::getConfigurationParameters)
                .extracting(edr -> edr.getSelectorsByType(DiscoverySelector.class)).asInstanceOf(InstanceOfAssertFactories.LIST)
                        .singleElement().asInstanceOf(InstanceOfAssertFactories.type(ClassSelector.class))
                        .returns(DiscoveryRequestMapper.class, ClassSelector::getJavaClass)
                ;
    }
}
