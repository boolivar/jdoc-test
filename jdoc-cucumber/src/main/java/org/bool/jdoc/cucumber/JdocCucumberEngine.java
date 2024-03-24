package org.bool.jdoc.cucumber;

import io.cucumber.junit.platform.engine.CucumberTestEngine;
import lombok.AllArgsConstructor;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;

import java.util.Optional;

@AllArgsConstructor
public class JdocCucumberEngine implements TestEngine {

    private final CucumberTestEngine cucumberEngine;

    private final DiscoveryRequestMapper requestMapper;

    public JdocCucumberEngine() {
        this(new CucumberTestEngine(), new DiscoveryRequestMapper());
    }

    @Override
    public String getId() {
        return "jdoc-cucumber";
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.of("org.bool.jdoc");
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        EngineDiscoveryRequest cucumberDiscoveryRequest = requestMapper.toCucumberDiscoveryRequest(discoveryRequest);
        return cucumberEngine.discover(cucumberDiscoveryRequest, uniqueId.appendEngine(cucumberEngine.getId()));
    }

    @Override
    public void execute(ExecutionRequest request) {
        cucumberEngine.execute(request);
    }
}
