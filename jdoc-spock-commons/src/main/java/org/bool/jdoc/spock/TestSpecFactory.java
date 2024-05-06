package org.bool.jdoc.spock;

import org.bool.jdoc.core.SpecSource;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TestSpecFactory {

    private final SpockSpecGenerator spockSpecGenerator;

    public TestSpecFactory() {
        this(new SpockSpecGenerator());
    }

    /**
     * Create test spec from spec source.
     * 
     * <pre><code lang="spock">
     * def "Generate spec from source"() {
     *   given:
     *     def thisFile = java.nio.file.Paths.get("src/main/java/org/bool/jdoc/spock/TestSpecFactory.java")
     *     def specFactory = new TestSpecFactory()
     *     def parser = new org.bool.jdoc.core.JavaFileParser("spock")
     *   when:
     *     def spec = specFactory.createTestSpec(parser.parse(thisFile), getClass().getClassLoader())
     *   then:
     *     with(spec.get()) {
     *       type == "spock"
     *       name == "TestSpecFactoryJdocSpockSpec"
     *       script.startsWith("package org.bool.jdoc.spock")
     *       script.contains("this line of test spec")
     *       script.contains("actually any text written here")
     *     }
     * }
     * </code></pre>
     */
    public Optional<TestSpec> createTestSpec(SpecSource specSource, ClassLoader classLoader) {
        return Optional.of(specSource).filter(source -> !source.getCodeBlocks().isEmpty())
                .map(source -> createSpec(source.getUnit(), source.getCodeBlocks(), classLoader));
    }

    @SneakyThrows
    private TestSpec createSpec(CompilationUnit unit, List<String> codeBlocks, ClassLoader classLoader) {
        Class<?> targetClass = classLoader.loadClass(unit.getPrimaryType().get().getFullyQualifiedName().get());
        return spockSpecGenerator.generateSpec(unit, codeBlocks, targetClass);
    }
}
