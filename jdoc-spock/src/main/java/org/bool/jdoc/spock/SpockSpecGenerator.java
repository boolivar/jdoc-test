package org.bool.jdoc.spock;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class SpockSpecGenerator {

    private final ClassIntrospector classIntrospector;

    public SpockSpecGenerator() {
        this(new ClassIntrospector());
    }

    /**
     * Generate test spec for targetClass using code in codeBlocks.
     * 
     * <pre><code lang="spock">
     * def "Spec generated for targetClass"() {
     *   given:
     *     classIntrospector.findMockConstructor(SpockSpecGenerator.class) >> SpockSpecGenerator.class.getConstructor(ClassIntrospector.class)
     *   when:
     *     def spec = $target.generateSpec(new CompilationUnit("org.bool.jdoc"), ['def "test method"() { }'], SpockSpecGenerator.class)
     *   then:
     *     spec.startsWith("package org.bool.jdoc")
     *     spec.contains("class SpockSpecGeneratorJdocSpockSpec extends Specification")
     *     spec.contains("def classIntrospector = Mock(ClassIntrospector)")
     *     spec.contains('def $target = new SpockSpecGenerator(classIntrospector)')
     *     spec.contains('def "test method"() { }')
     * }
     * </code></pre>
     */
    public String generateSpec(CompilationUnit unit, List<String> codeBlocks, Class<?> targetClass) {
        return generateSpec(unit, codeBlocks, targetClass, classIntrospector.findMockConstructor(targetClass));
    }

    public String generateSpec(CompilationUnit unit, List<String> codeBlocks, Class<?> targetClass, Constructor<?> ctor) {
        Builder<String> spec = Stream.builder();
        unit.getPackageDeclaration().map(Object::toString).ifPresent(spec);
        unit.getImports().stream().map(Object::toString).forEach(spec);
        spec.add(String.format("class %sJdocSpockSpec extends Specification {", targetClass.getSimpleName()));
        defMockFields(ctor).forEach(spec);
        defTargetField(ctor).ifPresent(spec);
        codeBlocks.forEach(spec);
        return spec.add("}").build().collect(joining("\n"));
    }

    private Stream<String> defMockFields(Constructor<?> ctor) {
        return IntStream.range(0, ctor.getParameterCount())
            .mapToObj(index -> String.format("def %s = Mock(%s)", ctor.getParameters()[index].getName(), ctor.getParameterTypes()[index].getSimpleName()));
    }

    private Optional<String> defTargetField(Constructor<?> ctor) {
        return Optional.of(String.format("def $target = new %s(%s)", ctor.getDeclaringClass().getSimpleName(),
                Stream.of(ctor.getParameters()).map(Parameter::getName).collect(joining(", "))));
    }
}
