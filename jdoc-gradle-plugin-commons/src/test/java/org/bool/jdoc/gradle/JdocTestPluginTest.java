package org.bool.jdoc.gradle;

import org.gradle.api.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JdocTestPluginTest {

    @Spy
    private JdocTestPlugin plugin;

    @Test
    void testVersions(@Mock Project project) {
        assertThat(plugin.loadProperties(project, "/jdoc-test-plugin.properties"))
            .containsEntry("someVersion", "1.2.3");
    }
}