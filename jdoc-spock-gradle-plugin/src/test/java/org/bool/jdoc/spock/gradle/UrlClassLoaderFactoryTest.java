package org.bool.jdoc.spock.gradle;

import org.bool.jdoc.spock.ResourceContainer;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.gradle.api.file.FileCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UrlClassLoaderFactoryTest {

    private final UrlClassLoaderFactory factory = new UrlClassLoaderFactory();

    @Test
    void testClassLoader(@Mock FileCollection files) throws Exception {
        var classPath = new File("build/classes/java");
        given(files.getFiles())
            .willReturn(Set.of(classPath));
        try (ResourceContainer<ClassLoader> container = factory.createClassLoader(files)) {
            assertThat(container.getResource())
                .isInstanceOf(URLClassLoader.class)
                .extracting("URLs", InstanceOfAssertFactories.ARRAY)
                .containsOnly(classPath.toURI().toURL());
        }
    }
}
