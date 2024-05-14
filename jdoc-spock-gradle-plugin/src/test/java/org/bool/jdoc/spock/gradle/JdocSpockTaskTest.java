package org.bool.jdoc.spock.gradle;

import org.bool.jdoc.spock.ResourceContainer;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.project.taskfactory.TaskIdentityFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.internal.execution.history.changes.DefaultFileChange;
import org.gradle.internal.file.FileType;
import org.gradle.internal.id.ConfigurationCacheableIdFactory;
import org.gradle.work.InputChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocSpockTaskTest {

    @TempDir
    private File tempDir;

    @Mock
    private Property<SourceDirectorySet> sources;

    @Mock
    private Property<String> langTag;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DirectoryProperty outputDir;

    @Mock
    private Property<FileCollection> classPath;

    @Mock
    private Function<FileCollection, ResourceContainer<ClassLoader>> classLoaderFactory;

    private JdocSpockTask task;

    @BeforeEach
    void init() {
        var project = mock(ProjectInternal.class, RETURNS_DEEP_STUBS);
        var identity = new TaskIdentityFactory(new ConfigurationCacheableIdFactory()).create("test-task", JdocSpockTask.class, project);
        task = JdocSpockTask.injectIntoNewInstance(project, identity, () -> new JdocSpockTask(sources, langTag, outputDir, classPath, classLoaderFactory));
    }

    @Test
    void testGenerateSpecs(@Mock InputChanges changes, @Mock FileCollection files, @Mock ClassLoader classLoader, @Mock SourceDirectorySet directorySet) throws Exception {
        var srcDir = new File("src/test/java");
        var srcFile = "src/test/java/org/bool/jdoc/spock/gradle/TestSpecClass.java";
        var fileChanges = List.of(DefaultFileChange.added(srcFile, "test-class", FileType.RegularFile, srcFile));

        given(langTag.get())
            .willReturn("test");

        given(sources.get())
            .willReturn(directorySet);
        given(directorySet.getSrcDirs())
            .willReturn(Set.of(srcDir));
        given(outputDir.get().getAsFile())
            .willReturn(tempDir);

        given(classPath.get())
            .willReturn(files);
        given(classLoaderFactory.apply(files))
            .willReturn(new ResourceContainer<>(classLoader));
        given(classLoader.loadClass(TestSpecClass.class.getName()))
            .willReturn((Class) TestSpecClass.class);

        given(changes.getFileChanges((Provider) sources))
            .willReturn(fileChanges);

        task.generateSpecs(changes);

        assertThat(tempDir.toPath().resolve("org/bool/jdoc/spock/gradle/TestSpecClassTestSpec.groovy"))
            .content().contains("test spec");
    }
}
