package org.bool.jdoc.gradle.cucumber;

import org.bool.jdoc.core.JavaFileParser;

import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.project.taskfactory.TaskIdentityFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.internal.execution.history.changes.DefaultFileChange;
import org.gradle.internal.file.FileType;
import org.gradle.internal.id.ConfigurationCacheableIdFactory;
import org.gradle.work.FileChange;
import org.gradle.work.InputChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocCucumberTaskTest {

    @Mock
    private Property<SourceDirectorySet> sources;

    @Mock
    private Property<String> langTag;

    @Mock
    private DirectoryProperty outputDir;

    @Mock
    private JdocCucumberGenerateAction generateAction;

    private JdocCucumberTask task;

    @BeforeEach
    void setupTask() {
        var project = mock(ProjectInternal.class, RETURNS_DEEP_STUBS);
        var identity = new TaskIdentityFactory(new ConfigurationCacheableIdFactory()).create("test-task", JdocCucumberTask.class, project);
        task = JdocCucumberTask.injectIntoNewInstance(project, identity, () -> new JdocCucumberTask(sources, langTag, outputDir, generateAction));
    }

    @Test
    void testGenerateFeatures(@Mock SourceDirectorySet sourceSet, @Mock Directory dir, @Mock InputChanges changes) {
        var file = new File("some.file");
        var fileChanges = List.<FileChange>of(DefaultFileChange.added("/", "test", FileType.RegularFile, "/"));

        given(sources.get())
            .willReturn(sourceSet);
        given(langTag.get())
            .willReturn("test-lang");
        given(outputDir.get())
            .willReturn(dir);
        given(changes.getFileChanges((Provider) sources))
            .willReturn(fileChanges);
        given(dir.getAsFile())
            .willReturn(file);

        assertThatNoException()
            .isThrownBy(() -> task.generateFeatures(changes));

        then(generateAction).should().generateFeatures(same(fileChanges), same(sourceSet), isA(JavaFileParser.class), eq(file.toPath()));
    }
}
