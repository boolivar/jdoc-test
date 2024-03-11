package org.bool.jdoc.spock;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spockframework.runtime.SpockEngine;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpockEngineServiceTest {

    @Mock
    private DiscoveryRequestMapper requestMapper;

    @Mock
    private SpecClassMapperFactory specClassMapperFactory;

    @InjectMocks
    private SpockEngineService service;

    @Test
    void testDiscover(@Mock SpockEngine spockEngine, @Mock ConfigurationParameters params, @Mock SpecClassMapper specClassMapper,
            @Mock TestDescriptor spockTestDescriptor) {
        var jdocDiscoveryRequest = DiscoveryRequest.builder().params(params).selectors(List.of()).build();
        var spockDiscoveryRequest = DiscoveryRequest.builder().params(params).selectors(List.of()).build();

        given(spockEngine.getId())
            .willReturn("spock-engine");
        given(specClassMapperFactory.create(params))
            .willReturn(specClassMapper);
        given(requestMapper.toSpockDiscoveryRequest(jdocDiscoveryRequest, specClassMapper))
            .willReturn(spockDiscoveryRequest);
        given(spockEngine.discover(spockDiscoveryRequest, UniqueId.forEngine("spock-engine")))
            .willReturn(spockTestDescriptor);

        assertThat(service.discover(spockEngine, jdocDiscoveryRequest, UniqueId.forEngine("jdoc-spock")))
                .extracting(JdocSpockEngineDescriptor::getChildren).asInstanceOf(InstanceOfAssertFactories.COLLECTION)
                .containsOnly(spockTestDescriptor)
                ;
    }
}
