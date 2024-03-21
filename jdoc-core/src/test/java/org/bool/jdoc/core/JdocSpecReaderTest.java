package org.bool.jdoc.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocSpecReaderTest {

    @TempDir
    private Path dir;

    @Mock
    private JavaFileParser javaFileParser;

    @InjectMocks
    private JdocSpecReader specReader;

    @Test
    void testSpec() throws IOException {
        var subdir = Files.createDirectories(dir.resolve("subdir"));
        var file1 = dir.resolve("File1.java");
        var file2 = subdir.resolve("File2.java");
        var file3 = subdir.resolve("File3.java");

        Files.createFile(file1);
        Files.createFile(file2);
        Files.createFile(file3);

        given(javaFileParser.parse(any()))
            .willReturn(new SpecSource());

        var specs = specReader.readSpecs(List.of(
                DiscoverySelectors.selectFile(file1.toFile()),
                DiscoverySelectors.selectFile(file2.toFile()),
                DiscoverySelectors.selectDirectory(subdir.toFile())
            )
        );

        assertThat(specs).hasSize(3);

        then(javaFileParser).should().parse(file1);
        then(javaFileParser).should().parse(file2);
        then(javaFileParser).should().parse(file3);
    }
}
