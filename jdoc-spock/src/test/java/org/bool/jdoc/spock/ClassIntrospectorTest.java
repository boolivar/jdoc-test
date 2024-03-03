package org.bool.jdoc.spock;

import org.bool.jdoc.spock.exception.SpockEngineException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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

        @SuppressWarnings("RedundantModifier")
        public AmbiguosClass(Consumer<String> consumer, Supplier<String> supplier) {
            this(supplier, consumer);
        }
    }

    @SuppressWarnings("PMD.LooseCoupling")
    @Test
    void testFindMockableConstructor() throws NoSuchMethodException {
        assertThat(introspector.findMockConstructor(ArrayList.class))
            .isEqualTo(ArrayList.class.getDeclaredConstructor(Collection.class));

        assertThat(introspector.findMockConstructor(String.class))
            .isEqualTo(String.class.getDeclaredConstructor());

        assertThatThrownBy(() -> introspector.findMockConstructor(LocalDate.class))
            .isInstanceOf(SpockEngineException.class)
            .hasMessageContaining("No mockable");

        assertThatThrownBy(() -> introspector.findMockConstructor(AmbiguosClass.class))
            .isInstanceOf(SpockEngineException.class)
            .hasMessageContaining("ambiguity");
    }
}
