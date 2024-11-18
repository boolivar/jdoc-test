package org.bool.jdoc.spock;

import org.bool.jdoc.core.SpecSource;

import groovy.lang.GroovyClassLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SpecClassMapperTest {

    @Mock
    private TestSpecFactory specFactory;

    @Mock
    private GroovyCompiler compiler;

    @Mock
    private GroovyClassLoader classLoader;

    @InjectMocks
    private SpecClassMapper mapper;

    @Test
    void testMapping() {
        var specSource = new SpecSource();

        given(specFactory.createTestSpec(specSource, classLoader))
                .willReturn(Optional.of(new TestSpec("spock", "spec", "{ code }")));
        given(compiler.compile("spec", "{ code }"))
                .willReturn(List.of(getClass()));
        assertThat(mapper.toTestSpecClasses(specSource))
                .containsOnly(getClass());
    }

    @Test
    void testCloseClassLoader() throws IOException {
        assertThatNoException().isThrownBy(mapper::close);
        then(classLoader).should().close();
    }
}
