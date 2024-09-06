package org.bool.jdoc.spock;

import org.bool.jdoc.core.SpecSource;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.NoSuchElementException;
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
     *       name == "TestSpecFactoryTestSpec"
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
        String className = unit.getPrimaryType().flatMap(TypeDeclaration::getFullyQualifiedName)
            .orElseThrow(() -> new NoSuchElementException("No primary type name found for " + unit));
        Class<?> targetClass = classLoader.loadClass(className);
        return spockSpecGenerator.generateSpec(unit, codeBlocks, targetClass);
    }
}
