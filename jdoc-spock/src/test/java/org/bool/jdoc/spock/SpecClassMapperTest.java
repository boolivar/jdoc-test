package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpecClassMapperTest {

    @Mock
    private JavaFileSpecFactory specFactory;

    @Mock
    private GroovyCompiler compiler;

    @Mock
    private GroovyClassLoader classLoader;

    @InjectMocks
    private SpecClassMapper mapper;

    @Test
    void testMapping() {
        var file = Paths.get("File.java");

        given(specFactory.createSpec(file, classLoader))
                .willReturn(Optional.of(new TestSpec("spock", "spec", "{ code }")));
        given(compiler.compile("spec", "{ code }"))
                .willReturn(List.of(getClass()));
        assertThat(mapper.toTestSpecClasses(file))
                .containsOnly(getClass());
    }
}
