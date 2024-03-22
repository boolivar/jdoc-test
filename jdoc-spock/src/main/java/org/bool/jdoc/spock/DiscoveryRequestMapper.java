package org.bool.jdoc.spock;

import org.bool.jdoc.core.DiscoveryRequest;
import org.bool.jdoc.core.JdocSpecReader;
import org.bool.jdoc.core.SpecSource;

import lombok.AllArgsConstructor;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bool.jdoc.spock.ConfigParams.*;

@AllArgsConstructor
public class DiscoveryRequestMapper {

    private final JdocSpecReader specReader;

    public DiscoveryRequestMapper() {
        this(new JdocSpecReader("spock"));
    }

    /**
     * Maps jdoc engine discovery request to spock engine discovery request.
     * 
     * <pre><code lang="spock">
     * def "file selector mapping"() {
     *   given:
     *     def params = Mock(org.junit.platform.engine.ConfigurationParameters)
     *     def specClassMapper = Mock(SpecClassMapper)
     *     def selectors = [DiscoverySelectors.selectFile("File.java")]
     *     def request = DiscoveryRequest.builder().selectors(selectors).params(params).build()
     *     def spec = new SpecSource()
     *
     *     params.get(ConfigParams.TEST_DIRS.key) >> Optional.empty()
     *     params.get(ConfigParams.TEST_FILES.key) >> Optional.empty()
     *     specReader.readSpecs(selectors) >> [spec]
     *     specClassMapper.toTestSpecClasses(spec) >> [getClass()]
     *   when:
     *     def result = $target.toSpockDiscoveryRequest(request, specClassMapper)
     *   then:
     *     result.params == params
     *     result.getSelectorsByType(DiscoverySelector.class).size() == 1
     *     result.getSelectorsByType(DiscoverySelector.class).get(0).javaClass == getClass()
     * }
     * </code></pre>
     */
    public EngineDiscoveryRequest toSpockDiscoveryRequest(EngineDiscoveryRequest request, SpecClassMapper specClassMapper) {
        List<SpecSource> specs = specReader.readSpecs(streamSelectors(request).collect(Collectors.toList()));
        List<DiscoverySelector> selectors = specs.stream()
                .flatMap(spec -> specClassMapper.toTestSpecClasses(spec).stream())
                .map(DiscoverySelectors::selectClass)
                .collect(Collectors.toList());
        return DiscoveryRequest.builder()
                .selectors(selectors)
                .params(request.getConfigurationParameters())
                .listener(request.getDiscoveryListener())
                .build();
    }

    private Stream<DiscoverySelector> streamSelectors(EngineDiscoveryRequest request) {
        return Stream.concat(
                request.getSelectorsByType(DiscoverySelector.class).stream(),
                Stream.concat(
                        TEST_DIRS.get(request.getConfigurationParameters()).stream().map(DiscoverySelectors::selectDirectory),
                        TEST_FILES.get(request.getConfigurationParameters()).stream().map(DiscoverySelectors::selectFile)
                )
        );
    }
}
