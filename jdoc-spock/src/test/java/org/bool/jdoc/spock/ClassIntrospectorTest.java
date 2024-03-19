package org.bool.jdoc.spock;

import org.bool.jdoc.spock.exception.SpockEngineException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassIntrospectorTest {

    private final ClassIntrospector introspector = new ClassIntrospector();

    @Getter
    @AllArgsConstructor
    static class AmbiguosClass {

        private final Supplier<String> supplier;

        private final Consumer<String> consumer;

        AmbiguosClass(Consumer<String> consumer, Supplier<String> supplier) {
            this(supplier, consumer);
        }
    }

    @Test
    void testFindMockableConstructor() throws NoSuchMethodException {
        assertThat(introspector.findMockConstructor(Exception.class))
            .hasValue(Exception.class.getDeclaredConstructor(Throwable.class));

        assertThat(introspector.findMockConstructor(String.class))
            .hasValue(String.class.getDeclaredConstructor());
    }

    @ValueSource(classes = { LocalDate.class, File.class })
    @ParameterizedTest
    void testNonMockable(Class<?> cls) {
        assertThat(introspector.findMockConstructor(File.class))
            .isEmpty();
    }

    @ValueSource(classes = AmbiguosClass.class)
    @ParameterizedTest
    void testAmbiguity(Class<?> cls) {
        assertThatThrownBy(() -> introspector.findMockConstructor(cls))
            .isInstanceOf(SpockEngineException.class)
            .hasMessageContaining("ambiguity");
    }
}
