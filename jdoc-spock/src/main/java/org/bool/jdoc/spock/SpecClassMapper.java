package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class SpecClassMapper {

    private final JavaFileSpecFactory specFactory;

    private final GroovyCompiler compiler;

    private final GroovyClassLoader classLoader;

    /**
     * Compile jdoc specs for file.
     * 
     * <pre><code lang="spock">
     * def "create and compile specs"() {
     *   given:
     *     def file = java.nio.file.Paths.get("File.java")
     *     specFactory.createSpec(file, classLoader) >> Optional.of(new TestSpec("spock", "spec", "def test() {}"))
     *     compiler.compile("spec", "def test() {}") >> [getClass()]
     *   when:
     *     def results = $target.toTestSpecClasses(file)
     *   then:
     *     results == [getClass()]
     * }
     * </code></pre>
     */
    public List<Class<?>> toTestSpecClasses(Path file) {
        return specFactory.createSpec(file, classLoader)
                .map(spec -> compiler.compile(spec.getName(), spec.getScript())).orElse(Collections.emptyList());
    }
}
