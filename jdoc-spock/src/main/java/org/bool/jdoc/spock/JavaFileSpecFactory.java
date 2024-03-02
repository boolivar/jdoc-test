package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;

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
     *     spec.startsWith("package org.bool.jdoc.spock")
     *     spec.contains("class SpockJavaFileSpecFactorySpec")
     *     spec.contains("this line of test spec")
     * }
     * </code></pre>
     */
    @SneakyThrows
    public String createSpec(Path file, ClassLoader classLoader) {
        SpecSource specSource = javaFileParser.parse(file);
        return specSource != null && !specSource.getCodeBlocks().isEmpty()
            ? createSpec(specSource.getUnit(), specSource.getCodeBlocks(), classLoader)
            : null;
    }

    private String createSpec(CompilationUnit unit, List<String> codeBlocks, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> targetClass = classLoader.loadClass(unit.getPrimaryType().get().getFullyQualifiedName().get());
        return spockSpecGenerator.generateSpec(unit, codeBlocks, targetClass);
    }
}
