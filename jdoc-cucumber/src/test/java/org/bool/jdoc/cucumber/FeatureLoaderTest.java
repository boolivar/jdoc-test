package org.bool.jdoc.cucumber;

import org.bool.jdoc.core.DiscoveryRequest;
import org.bool.jdoc.core.JdocSpecReader;
import org.bool.jdoc.core.SpecSource;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FeatureLoaderTest {

    @TempDir
    private Path dir;

    @Mock
    private JdocSpecReader specReader;

    @Mock
    private FeatureWriter featureWriter;

    @InjectMocks
    private FeatureLoader loader;

    @Test
    void testLoadFeatures(@Mock ConfigurationParameters params) throws IOException {
        var file = dir.resolve("File.java");
        var selectors = List.<DiscoverySelector>of(DiscoverySelectors.selectFile("File.java"));
        var request = DiscoveryRequest.builder().params(params).selectors(selectors).build();
        var unit = new CompilationUnit("org.bool");
        unit.setStorage(file);
        var specs = List.of(new SpecSource(unit, List.of("spec text")));

        Files.createFile(file);
        given(params.get(ConfigParams.OUTPUT_DIR.getKey())).willReturn(Optional.of(dir.toString()));
        given(specReader.readSpecs(selectors)).willReturn(specs);
        given(featureWriter.writeFeatures(dir.resolve("org/bool"), "File", List.of("spec text"), Files.getLastModifiedTime(file).toInstant()))
            .willReturn(List.of(dir.resolve("test.spec")));

        assertThat(loader.loadFeatures(request))
            .singleElement()
            .isEqualTo(dir.resolve("test.spec"));
    }
}
