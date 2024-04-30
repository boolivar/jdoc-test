package org.bool.jdoc.gradle.cucumber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class JdocCucumberDeleteActionTest {

    @TempDir
    Path temp;

    private final JdocCucumberDeleteAction action = new JdocCucumberDeleteAction();

    @Test
    void testDeleteFiles() throws IOException {
        Files.writeString(temp.resolve("File.feature"), "0");
        Files.writeString(temp.resolve("File_1.feature"), "1");
        Files.writeString(temp.resolve("File_2.feature"), "2");

        action.delete(temp, "File_", ".feature");

        assertThat(Files.list(temp).toList())
            .containsOnly(temp.resolve("File.feature"));
    }

    @Test
    void testDeleteFile() throws IOException {
        var file = temp.resolve("test.file");
        Files.writeString(file, "test");

        action.delete(file);

        assertThat(Files.list(temp).toList())
            .isEmpty();
    }

    @Test
    void testDeleteDir() throws IOException {
        Files.createDirectories(temp.resolve("test/features"));
        Files.writeString(temp.resolve("test/File_1.feature"), "1");
        Files.writeString(temp.resolve("test/features/File_2.feature"), "2");

        action.delete(temp.resolve("test"));

        assertThat(Files.list(temp).toList())
            .isEmpty();
    }

    @Test
    void testDeleteNonexistant() throws IOException {
        action.delete(temp.resolve("some.file"));

        action.delete(temp, "some", ".file");

        assertThat(Files.list(temp).toList())
            .isEmpty();
    }
}
