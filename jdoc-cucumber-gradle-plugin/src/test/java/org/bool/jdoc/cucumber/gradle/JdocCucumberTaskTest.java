package org.bool.jdoc.cucumber.gradle;

import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.project.taskfactory.TaskIdentityFactory;
import org.gradle.api.provider.Property;
import org.gradle.internal.execution.history.changes.DefaultFileChange;
import org.gradle.internal.file.FileType;
import org.gradle.internal.id.ConfigurationCacheableIdFactory;
import org.gradle.work.FileChange;
import org.gradle.work.InputChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocCucumberTaskTest {

    @TempDir
    private Path tempDir;

    @Mock
    private Property<FileCollection> sources;

    @Mock
    private Property<String> langTag;

    @Mock
    private DirectoryProperty outputDir;

    private JdocCucumberTask task;

    @BeforeEach
    void setupTask() {
        var project = mock(ProjectInternal.class, RETURNS_DEEP_STUBS);
        var identity = new TaskIdentityFactory(new ConfigurationCacheableIdFactory()).create("test-task", JdocCucumberTask.class, project);
        task = JdocCucumberTask.injectIntoNewInstance(project, identity, () -> new JdocCucumberTask(sources, langTag, outputDir));
    }

    @Test
    void testGenerateFeatures(@Mock SourceDirectorySet sourceSet, @Mock Directory dir, @Mock InputChanges changes) throws IOException {
        var sourceDir = tempDir.resolve("in");
        var targetDir = tempDir.resolve("out");
        var testFile = sourceDir.resolve("Test.java");
        var fileChanges = List.<FileChange>of(DefaultFileChange.added(testFile.toString(), "test", FileType.RegularFile, "/"));

        Files.createDirectories(sourceDir);
        Files.writeString(testFile, """
                /**
                 * <pre><code lang="test">TEST FEATURE</code></pre>
                 */
                public class Test {
                }
                """);

        given(langTag.get())
            .willReturn("test");
        given(changes.getFileChanges((Property) sources))
            .willReturn(fileChanges);

        given(sources.get())
            .willReturn(sourceSet);
        given(sourceSet.getFiles())
            .willReturn(Set.of(sourceDir.toFile()));
        given(outputDir.get())
            .willReturn(dir);
        given(dir.getAsFile())
            .willReturn(targetDir.toFile());

        task.generateFeatures(changes);

        assertThat(targetDir.resolve("Test_1.feature"))
            .content().isEqualTo("TEST FEATURE");
    }
}
