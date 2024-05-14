package org.bool.jdoc.gradle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JdocActionTest {

    @TempDir
    private Path tempDir;

    @Spy
    private JdocAction action;

    @Test
    void testDeleteDir() throws IOException {
        Files.createDirectories(tempDir.resolve("test/files"));
        Files.writeString(tempDir.resolve("test/File.txt"), "file");
        Files.writeString(tempDir.resolve("test/files/SubFile.txt"), "subfile");

        assertThat(tempDir)
            .isNotEmptyDirectory();
        assertThat(tempDir.resolve("test"))
            .isNotEmptyDirectory();
        assertThat(tempDir.resolve("test/files"))
            .isNotEmptyDirectory();

        action.deleteDir(new File("any"), tempDir.resolve("test"));

        assertThat(tempDir)
            .isEmptyDirectory();
    }
}
