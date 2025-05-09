package org.bool.jdoc.spock;

import org.bool.jdoc.spock.exception.SpockEngineException;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ClassIntrospector {

    /**
     * Biggest constructor containing all non-final parameter types.
     * 
     * <pre><code lang="spock">
     * def "mockable constructor"() {
     *   when:
     *     def ctor = $target.findMockConstructor(ArrayList).get()
     *   then:
     *     ctor.parameterTypes == [Collection]
     * }
     * 
     * def "default constructor"() {
     *   when:
     *     def ctor = $target.findMockConstructor(String).get()
     *   then:
     *     ctor.parameterTypes == []
     * }
     * 
     * def "no mockable constructor found"() {
     *   when:
     *     def ctor = $target.findMockConstructor(java.time.LocalTime)
     *   then:
     *     !ctor.isPresent()
     * }
     * </code></pre>
     */
    @SneakyThrows
    public <T> Optional<Constructor<T>> findMockConstructor(Class<T> targetClass) {
        NavigableMap<Integer, List<Constructor<?>>> ctors = Stream.of(targetClass.getDeclaredConstructors())
            .filter(this::canBeMocked)
            .collect(groupingBy(Constructor::getParameterCount, TreeMap::new, toList()));
        for (List<Constructor<?>> mockable : ctors.descendingMap().values()) {
            if (mockable.size() == 1) {
                return Optional.of(targetClass.getDeclaredConstructor(mockable.get(0).getParameterTypes()));
            }
            if (mockable.size() > 1) {
                throw new SpockEngineException(targetClass + " mockable constructors ambiguity: " + mockable);
            }
        }
        return Optional.empty();
    }

    private boolean canBeMocked(Constructor<?> ctor) {
        return !Modifier.isPrivate(ctor.getModifiers()) && Stream.of(ctor.getParameterTypes())
            .map(Class::getModifiers)
            .noneMatch(Modifier::isFinal);
    }
}
