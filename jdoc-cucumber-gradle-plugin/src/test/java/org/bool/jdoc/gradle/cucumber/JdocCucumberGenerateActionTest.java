package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.JavaFileParser;
import org.bool.jdoc.core.SpecSource;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.internal.execution.history.changes.DefaultFileChange;
import org.gradle.internal.file.FileType;
import org.gradle.work.FileChange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocCucumberGenerateActionTest {

    private final Path outputPath = Paths.get("/output");

    private final File srcDir = new File("/test");

    @Mock
    private SourceDirectorySet sources;

    @Mock
    private JavaFileParser parser;

    @Mock
    private JdocCucumberWriteSpecAction writeAction;

    @Mock
    private JdocCucumberDeleteAction deleteAction;

    @InjectMocks
    private JdocCucumberGenerateAction generateAction;

    @Test
    void testFilesAdded() {
        var changes = List.<FileChange>of(
                DefaultFileChange.added("/test/File1.java", "test1", FileType.RegularFile, ""),
                DefaultFileChange.added("/test/package/File2.java", "test2", FileType.RegularFile, ""));
        var spec1 = new SpecSource(null, List.of("spec1"));
        var spec2 = new SpecSource(null, List.of("spec2"));

        given(parser.parse(Paths.get("/test/File1.java")))
            .willReturn(spec1);
        given(parser.parse(Paths.get("/test/package/File2.java")))
            .willReturn(spec2);
        given(sources.getSrcDirs())
            .willReturn(Set.of(srcDir));

        assertThatNoException()
            .isThrownBy(() -> generateAction.generateFeatures(changes, sources, parser, outputPath));

        then(writeAction).should().writeSpecs(outputPath, "File1", spec1);
        then(writeAction).should().writeSpecs(outputPath.resolve("package"), "File2", spec2);
        then(deleteAction).shouldHaveNoInteractions();
    }

    @Test
    void testFilesModified() {
        var changes = List.<FileChange>of(DefaultFileChange.modified("/test/File.java", "test", FileType.RegularFile, FileType.RegularFile, ""));
        var spec = new SpecSource(null, List.of("spec"));

        given(parser.parse(Paths.get("/test/File.java")))
            .willReturn(spec);
        given(sources.getSrcDirs())
            .willReturn(Set.of(srcDir));

        assertThatNoException()
            .isThrownBy(() -> generateAction.generateFeatures(changes, sources, parser, outputPath));

        then(deleteAction).should().delete(outputPath, "File_", ".feature");
        then(writeAction).should().writeSpecs(outputPath, "File", spec);
    }

    @Test
    void testFilesRemoved() {
        var changes = List.<FileChange>of(
                DefaultFileChange.removed("/test/File.java", "test", FileType.RegularFile, ""),
                DefaultFileChange.removed("/test/package", "test", FileType.Directory, ""));

        given(sources.getSrcDirs())
            .willReturn(Set.of(srcDir));

        assertThatNoException()
            .isThrownBy(() -> generateAction.generateFeatures(changes, sources, parser, outputPath));

        then(deleteAction).should().delete(outputPath, "File_", ".feature");
        then(deleteAction).should().delete(outputPath.resolve("package"));
        then(parser).shouldHaveNoInteractions();
    }
}
