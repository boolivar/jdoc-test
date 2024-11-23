package org.bool.jdoc.core;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.FileSelector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class JdocSpecReader {

    private final JavaFileParser parser;

    public JdocSpecReader(String lang) {
        this(new JavaFileParser(lang));
    }

    /**
     * Collect the specs from selected java files.
     * 
     * <pre><code lang="spock">
     * def "Parse file specified by FileSelector"() {
     *   given:
     *     def spec = new SpecSource()
     *     parser.parse(java.nio.file.Paths.get("File.java")) >> spec
     *   when:
     *     def specs = $target.readSpecs([org.junit.platform.engine.discovery.DiscoverySelectors.selectFile("File.java")])
     *   then:
     *     specs == [spec]
     * }
     * </code></pre>
     */
    public List<SpecSource> readSpecs(List<DiscoverySelector> selectors) {
        return selectors.stream()
                .flatMap(this::streamFiles).distinct()
                .map(parser::parse).collect(Collectors.toList());
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
