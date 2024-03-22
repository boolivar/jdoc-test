package org.bool.jdoc.cucumber;

import io.cucumber.junit.platform.engine.CucumberTestEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JdocCucumberEngineTest {

    @Mock
    private CucumberTestEngine cucumberEngine;

    @Mock
    private DiscoveryRequestMapper requestMapper;

    @InjectMocks
    private JdocCucumberEngine jdocEngine;

    @Test
    void testDiscover(@Mock EngineDiscoveryRequest jdocRequest, @Mock EngineDiscoveryRequest cucumberRequest) {
        var descriptor = new EngineDescriptor(UniqueId.forEngine("cucumber-test"), "out");
        given(requestMapper.toCucumberDiscoveryRequest(jdocRequest))
            .willReturn(cucumberRequest);
        given(cucumberEngine.discover(cucumberRequest, UniqueId.forEngine("jdoc").appendEngine("cucumber-testid")))
            .willReturn(descriptor);
        given(cucumberEngine.getId())
            .willReturn("cucumber-testid");

        assertThat(jdocEngine.discover(jdocRequest, UniqueId.forEngine("jdoc")))
            .isSameAs(descriptor);
    }

    @Test
    void testExecute(@Mock ExecutionRequest request) {
        assertThatNoException().isThrownBy(() -> jdocEngine.execute(request));
        then(cucumberEngine).should().execute(request);
    }
}
