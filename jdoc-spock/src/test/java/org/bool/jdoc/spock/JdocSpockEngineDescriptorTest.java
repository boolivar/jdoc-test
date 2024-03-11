package org.bool.jdoc.spock;

import groovy.lang.GroovyClassLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.UniqueId;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JdocSpockEngineDescriptorTest {

    @Test
    void testCloseWithoutClassLoader() throws Exception {
        try (var descriptor = JdocSpockEngineDescriptor.builder()
                .displayName("DisplayName").uniqueId(UniqueId.forEngine("UniqueId")).build()) {
            assertThat(descriptor)
                   .returns("DisplayName", JdocSpockEngineDescriptor::getDisplayName)
                   .returns("UniqueId", d -> d.getUniqueId().getEngineId().orElseThrow());
        }
    }

    @Test
    void testCloseClassLoader(@Mock GroovyClassLoader classLoader) throws Exception {
        try (var descriptor = JdocSpockEngineDescriptor.builder()
                .displayName("Name").uniqueId(UniqueId.forEngine("Id")).resource(classLoader).build()) {
            assertThat(descriptor.getChildren())
                .isEmpty();
        }
        then(classLoader).should().close();
    }
}
