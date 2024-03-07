package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.spockframework.runtime.SpockEngine;

import java.util.Optional;

@AllArgsConstructor
public class JdocSpockEngine implements TestEngine {

    public static final String ID = "jdoc-spock";

    public static final String GROUP_ID = "org.bool.jdoc";

    private final SpockEngine spockEngine;

    private final SpockEngineService spockEngineService;

    public JdocSpockEngine() {
        this(new SpockEngine(), new SpockEngineService());
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.of(GROUP_ID);
    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * Discover jdoc-spock tests in java comments.
     * 
     * <pre><code lang="spock">
     * def "discover uses spockEngineService to discover JdocSpockEngineDescriptor"() {
     *   given:
     *     def uniqueId = UniqueId.forEngine("jdoc-spock")
     *     def discoveryRequest = Mock(EngineDiscoveryRequest)
     *     def engineDescriptor = Mock(JdocSpockEngineDescriptor)
     *     spockEngineService.discover(spockEngine, discoveryRequest, uniqueId) >> engineDescriptor
     *   when:
     *     def descriptor = $target.discover(discoveryRequest, uniqueId)
     *   then:
     *     descriptor == engineDescriptor
     * }
     * </code></pre>
     */
    @Override
    public JdocSpockEngineDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        return spockEngineService.discover(spockEngine, discoveryRequest, uniqueId);
    }

    @SneakyThrows
    @Override
    public void execute(ExecutionRequest request) {
        try (JdocSpockEngineDescriptor engineDescriptor = (JdocSpockEngineDescriptor) request.getRootTestDescriptor()) {
            for (TestDescriptor testDescriptor : engineDescriptor.getChildren()) {
                spockEngine.execute(ExecutionRequest.create(testDescriptor,
                        request.getEngineExecutionListener(), request.getConfigurationParameters()));
            }
        }
    }
}
