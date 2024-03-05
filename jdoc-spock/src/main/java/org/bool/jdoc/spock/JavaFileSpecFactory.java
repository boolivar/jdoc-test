package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JavaFileSpecFactory {

    private final JavaFileParser javaFileParser;

    private final SpockSpecGenerator spockSpecGenerator;

    public JavaFileSpecFactory() {
        this(new JavaFileParser(), new SpockSpecGenerator());
    }

    /**
     * Create test spec for java file.
     * 
     * <pre><code lang="spock">
     * def "Parse file and generate spec"() {
     *   given:
     *     def file = java.nio.file.Paths.get("src/main/java/org/bool/jdoc/spock/JavaFileSpecFactory.java")
     *     def factory = new JavaFileSpecFactory()
     *   when:
     *     def spec = factory.createSpec(file, getClass().getClassLoader())
     *   then:
     *     with(spec.get()) {
     *       type == "spock"
     *       name == "JavaFileSpecFactoryJdocSpockSpec"
     *       script.startsWith("package org.bool.jdoc.spock")
     *       script.contains("class SpockJavaFileSpecFactorySpec")
     *       script.contains("this line of test spec")
     *     }
     * }
     * </code></pre>
     */
    public Optional<TestSpec> createSpec(Path file, ClassLoader classLoader) {
        return Optional.of(javaFileParser.parse(file))
            .filter(specSource -> !specSource.getCodeBlocks().isEmpty())
            .map(specSource -> createSpec(specSource.getUnit(), specSource.getCodeBlocks(), classLoader));
    }

    @SneakyThrows
    private TestSpec createSpec(CompilationUnit unit, List<String> codeBlocks, ClassLoader classLoader) {
        Class<?> targetClass = classLoader.loadClass(unit.getPrimaryType().get().getFullyQualifiedName().get());
        return spockSpecGenerator.generateSpec(unit, codeBlocks, targetClass);
    }
}
