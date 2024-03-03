package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpockSpecGeneratorTest {

    @Mock
    private ClassIntrospector classIntrospector;

    @InjectMocks
    private SpockSpecGenerator generator;

    @MethodSource
    @ParameterizedTest
    void testSpec(Constructor<SpockSpecGenerator> ctor, String expectedDef) {
        given(classIntrospector.findMockConstructor(SpockSpecGenerator.class))
            .willReturn(ctor);

        assertThat(generator.generateSpec(new CompilationUnit("org.bool.jdoc"), List.of("def 'test'() {}"), SpockSpecGenerator.class))
            .contains(expectedDef);
    }

    static Stream<Arguments> testSpec() throws NoSuchMethodException {
        return Stream.of(
            Arguments.of(
                SpockSpecGenerator.class.getDeclaredConstructor(),
                "def $target = new SpockSpecGenerator()"
            ),
            Arguments.of(
                SpockSpecGenerator.class.getDeclaredConstructor(ClassIntrospector.class),
                "def $target = new SpockSpecGenerator(classIntrospector)"
            )
        );
    }
}
