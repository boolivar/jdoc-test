package org.bool.jdoc.spock;

import org.bool.jdoc.core.SpecSource;

import groovy.lang.GroovyClassLoader;
import lombok.AllArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class SpecClassMapper implements Closeable {

    private final TestSpecFactory specFactory;

    private final GroovyCompiler compiler;

    private final GroovyClassLoader classLoader;

    /**
     * Compile jdoc specs for file.
     * 
     * <pre><code lang="spock">
     * def "create and compile specs"() {
     *   given:
     *     def specSource = new SpecSource()
     *     specFactory.createTestSpec(specSource, classLoader) >> Optional.of(new TestSpec("spock", "spec", "def test() {}"))
     *     compiler.compile("spec", "def test() {}") >> [getClass()]
     *   when:
     *     def results = $target.toTestSpecClasses(specSource)
     *   then:
     *     results == [getClass()]
     * }
     * </code></pre>
     */
    public List<Class<?>> toTestSpecClasses(SpecSource specSource) {
        return specFactory.createTestSpec(specSource, classLoader)
                .map(spec -> compiler.compile(spec.getName(), spec.getScript())).orElse(Collections.emptyList());
    }

    @Override
    public void close() throws IOException {
        classLoader.close();
    }
}
