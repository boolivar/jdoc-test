package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JavaFileSpecFactoryTest {

    @Mock
    private JavaFileParser javaFileParser;

    @Mock
    private SpockSpecGenerator spockSpecGenerator;

    @InjectMocks
    private JavaFileSpecFactory factory;

    @Test
    void testCreateSpec(@Mock CompilationUnit unit, @Mock TypeDeclaration<?> typeDeclaration) {
        Path file = Paths.get("test");
        given(javaFileParser.parse(file))
            .willReturn(new SpecSource(unit, List.of("test code")));

        given(unit.getPrimaryType())
            .willReturn(Optional.of(typeDeclaration));
        given(typeDeclaration.getFullyQualifiedName())
            .willReturn(Optional.of(JavaFileSpecFactoryTest.class.getName()));

        given(spockSpecGenerator.generateSpec(unit, List.of("test code"), JavaFileSpecFactoryTest.class))
            .willReturn("Generated Spec");

        assertThat(factory.createSpec(file, ClassLoader.getSystemClassLoader()))
            .isEqualTo("Generated Spec");
    }
}