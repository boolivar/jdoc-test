package org.bool.jdoc.cucumber;

import org.bool.jdoc.core.JdocSpecReader;
import org.bool.jdoc.core.SpecSource;

import com.github.javaparser.ast.CompilationUnit.Storage;
import com.github.javaparser.utils.CodeGenerationUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bool.jdoc.cucumber.ConfigParams.*;

@AllArgsConstructor
public class FeatureLoader {

    private final JdocSpecReader specReader;

    private final FeatureWriter featureWriter;

    public FeatureLoader() {
        this(new JdocSpecReader("gherkin"), new FeatureWriter());
    }

    public List<Path> loadFeatures(EngineDiscoveryRequest request) {
        Path outputDir = OUTPUT_DIR.maybeGet(request.getConfigurationParameters()).map(Paths::get)
                .orElseGet(this::tempDir);
        List<DiscoverySelector> selectors = Stream.concat(
                request.getSelectorsByType(DiscoverySelector.class).stream(),
                Stream.concat(
                        TEST_DIRS.get(request.getConfigurationParameters()).stream().map(DiscoverySelectors::selectDirectory),
                        TEST_FILES.get(request.getConfigurationParameters()).stream().map(DiscoverySelectors::selectFile)
                )
        ).collect(Collectors.toList());
        return specReader.readSpecs(selectors).stream()
                .flatMap(spec -> loadFeatures(spec, outputDir))
                .collect(Collectors.toList());
    }

    private Stream<Path> loadFeatures(SpecSource spec, Path outputDir) {
        String name = spec.getUnit().getPrimaryTypeName().get();
        String pkg = spec.getUnit().getPackageDeclaration().map(pd -> pd.getName().toString()).orElse("");
        Path dir = CodeGenerationUtils.packageAbsolutePath(outputDir, pkg);
        Instant sourceTimestamp = spec.getUnit().getStorage().map(this::modifiedTime).orElse(Instant.MAX);
        return featureWriter.writeFeatures(dir, name, spec.getCodeBlocks(), sourceTimestamp).stream();
    }

    @SneakyThrows
    private Path tempDir() {
        return Files.createTempDirectory("jdoc-cucumber-");
    }

    @SneakyThrows
    private Instant modifiedTime(Storage storage) {
        return Files.getLastModifiedTime(storage.getPath()).toInstant();
    }
}
