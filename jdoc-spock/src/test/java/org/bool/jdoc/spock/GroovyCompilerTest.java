package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.io.ReaderSource;
import org.codehaus.groovy.tools.GroovyClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GroovyCompilerTest {

    private final CompilationUnit unit = mock(CompilationUnit.class);

    private final GroovyCompiler compiler = new GroovyCompiler(() -> unit);

    @Captor
    private ArgumentCaptor<SourceUnit> captor;

    @AfterEach
    void verifyCompile() {
        then(unit).should().compile();
        then(unit).should().getClasses();
    }

    @Test
    void testCompile(@Mock GroovyClassLoader classLoader, @Mock ReaderSource readerSource) {
        var testClass = new GroovyClass("test-class", "test".getBytes());

        given(unit.getClassLoader())
            .willReturn(classLoader);
        given(unit.getClasses())
            .willReturn(List.of(testClass));
        given(classLoader.defineClass(testClass.getName(), testClass.getBytes()))
            .willReturn(getClass());

        assertThat(compiler.compile(Map.of("In", readerSource)))
            .singleElement()
            .isSameAs(getClass());
        
        then(unit).should().addSource(captor.capture());
        assertThat(captor.getValue())
            .returns("In", SourceUnit::getName)
            .returns(readerSource, SourceUnit::getSource)
            ;
    }

    @Test
    void testCompileString() {
        compiler.compile("In", "Script");
        then(unit).should().addSource("In", "Script");
    }

    @Test
    void testCompileInputStream() {
        var in = new ByteArrayInputStream("Script".getBytes());
        compiler.compile("In", in);
        then(unit).should().addSource("In", in);
    }

    @Test
    void testCompileReaderSource(@Mock ReaderSource readerSource) {
        compiler.compile("In", readerSource);

        then(unit).should().addSource(captor.capture());
        assertThat(captor.getValue())
            .returns("In", SourceUnit::getName)
            .returns(readerSource, SourceUnit::getSource)
            ;
    }
}
