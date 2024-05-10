package org.bool.jdoc.gradle.spock;

import org.bool.jdoc.spock.TestSpec;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JdocSpockActionTest {

    @TempDir
    private Path outputPath;

    @Mock
    private ClassLoader classLoader;

    @Mock
    private SpecGenerator specGenerator;

    @InjectMocks
    private JdocSpockAction action;

    @Test
    void testGenerate() {
        var file = new File("/some/File.java");
        var testSpec = new TestSpec("test", "spec", "class TestSpec { }");

        given(specGenerator.generateSpec(file.toPath(), classLoader))
            .willReturn(Optional.of(testSpec));

        action.generate(file, "File", outputPath);

        assertThat(outputPath.resolve("FileTestSpec.groovy"))
            .content().isEqualTo("class TestSpec { }");
    }

    @Test
    void testDeleteFile() throws IOException {
        var file = outputPath.resolve("FileNameTestSpec.groovy");

        Files.writeString(file, "test");
        assertThat(file)
            .exists();

        action.delete(new File("File.java"), "FileName", outputPath);
        assertThat(file)
            .doesNotExist();
    }

    @Test
    void testDeleteDir() throws IOException {
        Files.createDirectories(outputPath.resolve("src/java"));
        var file1 = outputPath.resolve("src/FileTestSpec.groovy");
        var file2 = outputPath.resolve("src/java/FileTestSpec.groovy");

        Files.writeString(file1, "test");
        Files.writeString(file2, "test");
        assertThat(file1)
            .exists();
        assertThat(file2)
            .exists();

        action.deleteDir(new File("File.java"), outputPath.resolve("src"));
        assertThat(outputPath)
            .isEmptyDirectory();
    }
}
