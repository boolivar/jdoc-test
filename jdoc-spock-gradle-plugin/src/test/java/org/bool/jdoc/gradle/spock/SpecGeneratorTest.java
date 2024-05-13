package org.bool.jdoc.gradle.spock;

import org.bool.jdoc.core.JavaFileParser;
import org.bool.jdoc.core.SpecSource;
import org.bool.jdoc.spock.TestSpec;
import org.bool.jdoc.spock.TestSpecFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpecGeneratorTest {

    @Mock
    private JavaFileParser parser;

    @Mock
    private TestSpecFactory specFactory;

    @InjectMocks
    private SpecGenerator generator;

    @Test
    void testGenerateSpec(@Mock ClassLoader classLoader) {
        var file = Path.of("File.java");
        var specSource = new SpecSource();
        var testSpec = new TestSpec();

        given(parser.parse(file))
            .willReturn(specSource);
        given(specFactory.createTestSpec(specSource, classLoader))
            .willReturn(Optional.of(testSpec));

        assertThat(generator.generateSpec(file, classLoader))
            .contains(testSpec);
    }
}
