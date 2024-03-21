package org.bool.jdoc.cucumber;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeatureWriterTest {

    @TempDir
    private static Path outDir;

    @InjectMocks
    private FeatureWriter writer;

    @Test
    void testWriteFeatures() {
        var past = Instant.parse("2007-12-03T10:15:30Z");

        var files = writer.writeFeatures(outDir, "TestSpec", List.of("test text"), past);

        assertThat(files)
            .singleElement(InstanceOfAssertFactories.PATH)
            .isRegularFile()
            .hasFileName("TestSpec1.feature")
            .hasContent("test text")
            ;

        assertThat(writer.writeFeatures(outDir, "TestSpec", List.of("new content"), past))
            .isEqualTo(files)
            .singleElement(InstanceOfAssertFactories.PATH)
            .hasContent("test text")
            ;

        assertThat(writer.writeFeatures(outDir, "TestSpec", List.of("new content"), Instant.MAX))
            .isEqualTo(files)
            .singleElement(InstanceOfAssertFactories.PATH)
            .hasContent("new content")
            ;
    }
}
