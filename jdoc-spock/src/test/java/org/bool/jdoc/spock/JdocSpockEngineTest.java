package org.bool.jdoc.spock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spockframework.runtime.SpockEngine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JdocSpockEngineTest {

    @Mock
    private SpockEngine spockEngine;

    @Mock
    private SpockEngineService spockEngineService;

    @InjectMocks
    private JdocSpockEngine engine;

    @Test
    void testDiscover(@Mock EngineDiscoveryRequest request) throws Exception {
        var id = UniqueId.forEngine(engine.getId());
        try (var descriptor = JdocSpockEngineDescriptor.builder().uniqueId(id).displayName("jdoc-test").build()) {
            given(spockEngineService.discover(spockEngine, request, id))
                .willReturn(descriptor);
            assertThat(engine.discover(request, id))
                .isEqualTo(descriptor);
        }
    }

    @Test
    void testExecute(@Mock ExecutionRequest request, @Mock TestDescriptor testDescriptor) throws Exception {
        var id = UniqueId.forEngine(engine.getId());
        try (var descriptor = JdocSpockEngineDescriptor.builder().uniqueId(id).displayName("jdoc-test").build()) {
            descriptor.addChild(testDescriptor);

            given(request.getRootTestDescriptor())
                .willReturn(descriptor);

            engine.execute(request);

            ArgumentCaptor<ExecutionRequest> captor = ArgumentCaptor.forClass(ExecutionRequest.class);
            then(spockEngine).should().execute(captor.capture());

            assertThat(captor.getValue().getRootTestDescriptor())
                .isSameAs(testDescriptor);
        }
    }
}
