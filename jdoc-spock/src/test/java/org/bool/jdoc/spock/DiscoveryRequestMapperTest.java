package org.bool.jdoc.spock;

import org.bool.jdoc.core.DiscoveryRequest;
import org.bool.jdoc.core.JdocSpecReader;
import org.bool.jdoc.core.SpecSource;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DiscoveryRequestMapperTest {

    @Mock
    private JdocSpecReader specReader;

    @InjectMocks
    private DiscoveryRequestMapper requestMapper;

    @Test
    void testMapping(@Mock ConfigurationParameters params, @Mock SpecClassMapper classMapper) {
        var selectors = List.<DiscoverySelector>of(DiscoverySelectors.selectFile("File.java"));
        var request = DiscoveryRequest.builder()
                .params(params).selectors(selectors).build();
        var specSource = new SpecSource();

        given(specReader.readSpecs(selectors))
                .willReturn(List.of(specSource));
        given(classMapper.toTestSpecClasses(specSource))
                .willReturn(List.of(DiscoveryRequestMapper.class));

        assertThat(requestMapper.toSpockDiscoveryRequest(request, classMapper))
                .returns(params, EngineDiscoveryRequest::getConfigurationParameters)
                .extracting(edr -> edr.getSelectorsByType(DiscoverySelector.class)).asInstanceOf(InstanceOfAssertFactories.LIST)
                        .singleElement().asInstanceOf(InstanceOfAssertFactories.type(ClassSelector.class))
                        .returns(DiscoveryRequestMapper.class, ClassSelector::getJavaClass)
                ;
    }
}
