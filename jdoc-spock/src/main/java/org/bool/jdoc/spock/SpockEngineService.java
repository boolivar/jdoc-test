package org.bool.jdoc.spock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.spockframework.runtime.SpockEngine;

@AllArgsConstructor
public class SpockEngineService {

    private final DiscoveryRequestMapper requestMapper;

    private final SpecClassMapperFactory specClassMapperFactory;

    public SpockEngineService() {
        this(new DiscoveryRequestMapper(), new SpecClassMapperFactory());
    }

    /**
     * Run spockEngine.discovery using jdoc-spock discovery request.
     * 
     * <pre><code lang="spock">
     * def "Invokes spockEngine.discovery"() {
     *   given:
     *     def spockEngine = Mock(SpockEngine)
     *     def spockTestDescriptor = Mock(TestDescriptor)
     *     spockEngine.id >> "spock"
     *     spockEngine.discover(_, _) >> spockTestDescriptor
     *   when:
     *     def result = $target.discover(spockEngine, Mock(EngineDiscoveryRequest), UniqueId.forEngine("jdoc-spock"))
     *   then:
     *     result.uniqueId.engineId.get() == "jdoc-spock"
     *     result.children == [spockTestDescriptor] as Set
     * }
     * </code></pre>
     */
    @SneakyThrows
    public JdocSpockEngineDescriptor discover(SpockEngine spockEngine, EngineDiscoveryRequest request, UniqueId uniqueId) {
        try (SpecClassMapper mapper = specClassMapperFactory.createMapper(request.getConfigurationParameters())) {
            EngineDiscoveryRequest discoveryRequest = requestMapper.toSpockDiscoveryRequest(request, mapper);
            TestDescriptor testDescriptor = spockEngine.discover(discoveryRequest, uniqueId.appendEngine(spockEngine.getId()));
            return createEngineDescriptor(uniqueId, testDescriptor);
        }
    }

    private JdocSpockEngineDescriptor createEngineDescriptor(UniqueId uniqueId, TestDescriptor testDescriptor) {
        JdocSpockEngineDescriptor engineDescriptor = JdocSpockEngineDescriptor.builder()
                .uniqueId(uniqueId)
                .displayName("jdock-spock")
                .build();
        engineDescriptor.addChild(testDescriptor);
        return engineDescriptor;
    }
}
