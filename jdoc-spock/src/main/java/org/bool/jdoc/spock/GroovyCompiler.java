package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import lombok.AllArgsConstructor;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.io.ReaderSource;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GroovyCompiler {

    private final Supplier<CompilationUnit> compilationUnit;

    public GroovyCompiler(CompilerConfiguration config, GroovyClassLoader classLoader) {
        this(() -> new CompilationUnit(config, null, classLoader));
    }

    /**
     * Compile groovy script.
     * 
     * <pre><code lang="spock">
     * def "Compile the script"() {
     *   given:
     *     def classLoader = new GroovyClassLoader()
     *     def compiler = new GroovyCompiler(new CompilerConfiguration(), classLoader)
     *   when:
     *     def results = compiler.compile("Hello World", "class Hello { def print() { 'World' } }")
     *     classLoader.close()
     *   then:
     *     results.size() == 1
     *     results[0].simpleName == "Hello"
     *     results[0].newInstance().print() == "World"
     * }
     * </code></pre>
     */
    public List<Class<?>> compile(String name, String script) {
        return compile(unit -> unit.addSource(name, script));
    }

    public List<Class<?>> compile(String name, InputStream script) {
        return compile(unit -> unit.addSource(name, script));
    }

    public List<Class<?>> compile(String name, ReaderSource script) {
        return compile(Collections.singletonMap(name, script));
    }

    public List<Class<?>> compile(Map<String, ReaderSource> scripts) {
        return compile(unit -> scripts.forEach((name, script) -> unit.addSource(sourceUnit(name, script, unit))));
    }

    private SourceUnit sourceUnit(String name, ReaderSource script, CompilationUnit unit) {
        return new SourceUnit(name, script, unit.getConfiguration(), unit.getClassLoader(), unit.getErrorCollector());
    }

    private List<Class<?>> compile(Consumer<CompilationUnit> sources) {
        CompilationUnit unit = compilationUnit.get();
        sources.accept(unit);
        unit.compile();
        return unit.getClasses().stream()
            .map(cls -> (Class<?>) unit.getClassLoader().defineClass(cls.getName(), cls.getBytes()))
            .collect(Collectors.toList());
    }
}
