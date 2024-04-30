package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.SpecSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdocCucumberWriteSpecActionTest {

    @TempDir
    Path temp;

    private final JdocCucumberWriteSpecAction action = new JdocCucumberWriteSpecAction();

    @Test
    void testWriteSpecs() throws IOException {
        var path = temp.resolve("test");

        action.writeSpecs(path, "TestFile", new SpecSource(null, List.of("spec1", "spec2")));

        assertThat(Files.list(path).toList())
            .hasSize(2);
        assertThat(path.resolve("TestFile_1.feature"))
            .content().isEqualTo("spec1");
        assertThat(path.resolve("TestFile_2.feature"))
            .content().isEqualTo("spec2");
    }

    @Test
    void testEmptySpec() throws IOException {
        action.writeSpecs(temp.resolve("any"), "Test", new SpecSource(null, List.of()));

        assertThat(Files.list(temp).toList())
            .isEmpty();
    }
}
