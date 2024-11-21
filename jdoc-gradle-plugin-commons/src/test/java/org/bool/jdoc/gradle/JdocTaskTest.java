package org.bool.jdoc.gradle;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocTaskTest {

    private final File srcDir = new File("/test");

    private final File outputPath = new File("/output");

    @Mock
    private SourceDirectorySet directorySet;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private Property<FileCollection> sources;

    @Mock
    private Property<String> langTag;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DirectoryProperty outputDir;

    private JdocTask task;

    @BeforeEach
    void setupTask() {
        var project = mock(ProjectInternal.class, RETURNS_DEEP_STUBS);
        var identity = new TaskIdentityFactory(new ConfigurationCacheableIdFactory()).create("test-task", JdocTask.class, project);
        task = JdocTask.injectIntoNewInstance(project, identity, () -> new JdocTask(sources, langTag, outputDir));

        when(sources.get())
            .thenReturn(directorySet);
    }

    @CsvSource({
        "some-file.java, some-file",
        "/absolute/path/to/File.class, File",
        "relative/path/to/SomeFile, SomeFile",
    })
    @ParameterizedTest
    void testBaseName(File file, String baseName) {
        assertThat(task.baseName(file))
            .isEqualTo(baseName);
    }

    @Test
    void testRelativizeUnrelatedPath() {
        var testFile = Paths.get("org/bool/file/Test.java");
        given(directorySet.getFiles())
            .willReturn(Set.of(new File("src/main/java")));

        assertThatThrownBy(() -> task.relativizePath(testFile))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("Test.java");
    }

    @Test
    void testFilesAdded(@Mock InputChanges input, @Mock JdocAction action) {
        var changes = List.<FileChange>of(
                DefaultFileChange.added("/test/File1.java", "test1", FileType.RegularFile, ""),
                DefaultFileChange.added("/test/package/File2.java", "test2", FileType.RegularFile, ""));

        given(directorySet.getFiles())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Property) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath);

        assertThatNoException()
            .isThrownBy(() -> task.generate(input, action));

        then(action).should().generate(new File("/test/File1.java"), "File1", outputPath.toPath());
        then(action).should().generate(new File("/test/package/File2.java"), "File2", outputPath.toPath().resolve("package"));
    }

    @Test
    void testFilesModified(@Mock InputChanges input, @Mock JdocAction action) {
        var testFile = new File("/test/File.java");
        var changes = List.<FileChange>of(DefaultFileChange.modified(testFile.toString(), "test", FileType.RegularFile, FileType.RegularFile, ""));

        given(directorySet.getFiles())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Property) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath);

        assertThatNoException()
            .isThrownBy(() -> task.generate(input, action));

        then(action).should().delete(testFile, "File", outputPath.toPath());
        then(action).should().generate(testFile, "File", outputPath.toPath());
    }

    @Test
    void testFilesRemoved(@Mock InputChanges input, @Mock JdocAction action) {
        var testFile = new File("/test/File.java");
        var testDir = new File("/test/package");
        var changes = List.<FileChange>of(
                DefaultFileChange.removed(testFile.toString(), "testfile", FileType.RegularFile, ""),
                DefaultFileChange.removed(testDir.toString(), "testdir", FileType.Directory, ""));

        given(directorySet.getFiles())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Property) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath);

        assertThatNoException()
            .isThrownBy(() -> task.generate(input, action));

        then(action).should().delete(testFile, "File", outputPath.toPath());
        then(action).should().deleteDir(testDir, outputPath.toPath().resolve("package"));
    }
}
