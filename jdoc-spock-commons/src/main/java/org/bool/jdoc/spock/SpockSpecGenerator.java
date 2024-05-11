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

    private static final String LF = "\n";

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
     *     classIntrospector.findMockConstructor(SpockSpecGenerator.class) >> Optional.of(SpockSpecGenerator.class.getConstructor(ClassIntrospector.class))
     *   when:
     *     def spec = $target.generateSpec(new CompilationUnit("org.bool.jdoc"), ['def "test method"() { }'], SpockSpecGenerator.class)
     *   then:
     *     with (spec) {
     *       type == "spock"
     *       name == "SpockSpecGeneratorJdocSpockSpec"
     *       script.startsWith("package org.bool.jdoc")
     *       script.contains("class $name extends Specification")
     *       script.contains("def classIntrospector = Mock(ClassIntrospector)")
     *       script.contains('def $target = new SpockSpecGenerator(classIntrospector)')
     *       script.contains('def "test method"() { }')
     *     }
     * }
     * </code></pre>
     */
    public TestSpec generateSpec(CompilationUnit unit, List<String> codeBlocks, Class<?> targetClass) {
        return generateSpec(unit, codeBlocks, targetClass, classIntrospector.findMockConstructor(targetClass).orElse(null));
    }

    public TestSpec generateSpec(CompilationUnit unit, List<String> codeBlocks, Class<?> targetClass, Constructor<?> ctor) {
        String name = String.format("%sJdocSpockSpec", targetClass.getSimpleName());
        return new TestSpec("spock", name, generateSpec(name, unit, codeBlocks, ctor));
    }

    private String generateSpec(String name, CompilationUnit unit, List<String> codeBlocks, Constructor<?> ctor) {
        Builder<String> spec = Stream.builder();
        unit.getPackageDeclaration().map(Object::toString).ifPresent(spec);
        spec.add("import spock.lang.*").add(LF);
        unit.getImports().stream().map(Object::toString).forEach(spec);
        spec.add(String.format("class %s extends Specification {", name));
        defMockFields(ctor).forEach(spec);
        defTargetField(ctor).ifPresent(spec);
        codeBlocks.forEach(spec);
        return spec.add("}").build().collect(joining(LF));
    }

    private Stream<String> defMockFields(Constructor<?> ctor) {
        return IntStream.range(0, ctor != null ? ctor.getParameterCount() : 0)
            .mapToObj(index -> String.format("def %s = Mock(%s)", ctor.getParameters()[index].getName(), ctor.getParameterTypes()[index].getSimpleName()));
    }

    private Optional<String> defTargetField(Constructor<?> constructor) {
        return Optional.ofNullable(constructor)
                .map(ctor -> String.format("def $target = new %s(%s)",
                        ctor.getDeclaringClass().getSimpleName(),
                        Stream.of(ctor.getParameters()).map(Parameter::getName).collect(joining(", "))));
    }
}
