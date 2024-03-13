package org.bool.jdoc.spock;

import lombok.SneakyThrows;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.FileSelector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bool.jdoc.spock.ConfigParams.*;

public class DiscoveryRequestMapper {

    /**
     * Maps jdoc engine discovery request to spock engine discovery request.
     * 
     * <pre><code lang="spock">
     * @TempDir
     * def tempDir;
     * def "file selector mapping"() {
     *   given:
     *     def params = Mock(org.junit.platform.engine.ConfigurationParameters)
     *     def specClassMapper = Mock(SpecClassMapper)
     *     def request = DiscoveryRequest.builder()
     *             .selectors([DiscoverySelectors.selectFile(tempDir.resolve("File.java").toString())])
     *             .params(params)
     *             .build();
     *     params.get(ConfigParams.TEST_DIRS.key) >> Optional.empty()
     *     params.get(ConfigParams.TEST_FILES.key) >> Optional.empty()
     *     specClassMapper.toTestSpecClasses(tempDir.resolve("File.java")) >> [getClass()]
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
        List<DiscoverySelector> selectors = streamSelectors(request)
                .flatMap(this::streamFiles)
                .flatMap(file -> specClassMapper.toTestSpecClasses(file).stream())
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

    @SneakyThrows
    private Stream<Path> streamFiles(DiscoverySelector selector) {
        if (selector instanceof DirectorySelector) {
            return Files.walk(((DirectorySelector) selector).getPath())
                    .filter(path -> path.getFileName().toString().endsWith(".java"));
        }
        if (selector instanceof FileSelector) {
            return Stream.of(((FileSelector) selector).getPath());
        }
        return Stream.empty();
    }
}
