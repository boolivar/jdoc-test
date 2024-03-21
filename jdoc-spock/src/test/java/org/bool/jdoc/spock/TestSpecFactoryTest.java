package org.bool.jdoc.spock;

import org.bool.jdoc.core.SpecSource;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TestSpecFactoryTest {

    @Mock
    private SpockSpecGenerator specGenerator;

    @InjectMocks
    private TestSpecFactory specFactory;

    @Test
    void testCreate(@Mock CompilationUnit unit, @Mock TypeDeclaration<?> typeDeclaration) {
        var testSpec = new TestSpec();
        var specSource = SpecSource.builder().unit(unit).codeBlocks(List.of("test code")).build();

        given(unit.getPrimaryType())
            .willReturn(Optional.of(typeDeclaration));
        given(typeDeclaration.getFullyQualifiedName())
            .willReturn(Optional.of(TestSpecFactory.class.getName()));
        given(specGenerator.generateSpec(unit, List.of("test code"), TestSpecFactory.class))
            .willReturn(testSpec);

        assertThat(specFactory.createTestSpec(specSource, Thread.currentThread().getContextClassLoader()))
            .hasValue(testSpec);
    }

    @Test
    void testEmpty() {
        var specSource = SpecSource.builder().codeBlocks(List.of()).build();
        assertThat(specFactory.createTestSpec(specSource, Thread.currentThread().getContextClassLoader()))
            .isEmpty();
    }
}
