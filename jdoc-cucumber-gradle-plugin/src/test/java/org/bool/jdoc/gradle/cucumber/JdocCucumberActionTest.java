package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.JavaFileParser;
import org.bool.jdoc.core.SpecSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocCucumberActionTest {

    @TempDir
    private Path outputPath;

    @Mock
    private JavaFileParser parser;

    @InjectMocks
    private JdocCucumberAction action;

    @Test
    void testGenerate() throws IOException {
        var file = Paths.get("/test/File1.java");
        var spec = new SpecSource(null, List.of("test", "spec"));

        given(parser.parse(file))
            .willReturn(spec);

        action.generate(file.toFile(), "File", outputPath);

        assertThat(Files.list(outputPath).toList())
            .hasSize(2);
        assertThat(outputPath.resolve("File_1.feature"))
            .content().isEqualTo("test");
        assertThat(outputPath.resolve("File_2.feature"))
            .content().isEqualTo("spec");
    }

    @Test
    void testDelete() throws IOException {
        Files.writeString(outputPath.resolve("TestFile_1.feature"), "feature1");
        Files.writeString(outputPath.resolve("TestFile_2.feature"), "feature2");
        Files.writeString(outputPath.resolve("TestFile_C.feature"), "featureC");

        Files.writeString(outputPath.resolve("TestFile.feature"), "unknown feature");
        Files.writeString(outputPath.resolve("TestFile_2.java"), "javafile");

        assertThat(Files.list(outputPath).toList())
            .hasSize(5);

        action.delete(new File("any-file.java"), "TestFile", outputPath);

        assertThat(Files.list(outputPath).toList())
            .hasSize(2)
            .doesNotContain(
                    outputPath.resolve("TestFile_1.feature"),
                    outputPath.resolve("TestFile_2.feature"),
                    outputPath.resolve("TestFile_C.feature"));
    }

    @Test
    void testDeleteDir() throws IOException {
        var testDir = outputPath.resolve("test");
        var subDir = testDir.resolve("features");
        Files.createDirectories(subDir);

        Files.writeString(testDir.resolve("File.feature"), "feature1");
        Files.writeString(subDir.resolve("SomeFile.txt"), "feature2");

        action.deleteDir(new File("any/dir"), testDir);

        assertThat(outputPath)
            .isEmptyDirectory();
    }
}
