package org.bool.jdoc.gradle;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JdocTaskTest {

    private final File srcDir = new File("/test");

    private final Path outputPath = Paths.get("/output");

    @Mock
    private SourceDirectorySet directorySet;

    @Mock(strictness = Strictness.LENIENT)
    private Property<SourceDirectorySet> sources;

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
        "/build/path/Main.java, Main",
        "path/to/Java, Java",
        "../root/task/Test.class, Test",
        "D:\\disk\\java\\Class.java, Class"
    })
    @ParameterizedTest
    void testBaseName(String path, String baseName) {
        assertThat(task.baseName(new File(path)))
            .isEqualTo(baseName);
    }

    @Test
    void testRelativizeUnrelatedPath() {
        given(directorySet.getSrcDirs())
            .willReturn(Set.of(new File("src/main/java")));

        assertThatThrownBy(() -> task.relativizePath(Paths.get("org/bool/file/Test.java")))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("Test.java");
    }

    @Test
    void testFilesAdded(@Mock InputChanges input, @Mock JdocAction action) {
        var changes = List.<FileChange>of(
                DefaultFileChange.added("/test/File1.java", "test1", FileType.RegularFile, ""),
                DefaultFileChange.added("/test/package/File2.java", "test2", FileType.RegularFile, ""));

        given(directorySet.getSrcDirs())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Provider) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath.toFile());

        task.generate(input, action);

        then(action).should().generate(new File("/test/File1.java"), "File1", outputPath);
        then(action).should().generate(new File("/test/package/File2.java"), "File2", outputPath.resolve("package"));
    }

    @Test
    void testFilesModified(@Mock InputChanges input, @Mock JdocAction action) {
        var changes = List.<FileChange>of(DefaultFileChange.modified("/test/File.java", "test", FileType.RegularFile, FileType.RegularFile, ""));

        given(directorySet.getSrcDirs())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Provider) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath.toFile());

        task.generate(input, action);

        then(action).should().delete(new File("/test/File.java"), "File", outputPath);
        then(action).should().generate(new File("/test/File.java"), "File", outputPath);
    }

    @Test
    void testFilesRemoved(@Mock InputChanges input, @Mock JdocAction action) {
        var changes = List.<FileChange>of(
                DefaultFileChange.removed("/test/File.java", "test", FileType.RegularFile, ""),
                DefaultFileChange.removed("/test/package", "test", FileType.Directory, ""));

        given(directorySet.getSrcDirs())
            .willReturn(Set.of(srcDir));
        given(input.getFileChanges((Provider) sources))
            .willReturn(changes);
        given(outputDir.get().getAsFile())
            .willReturn(outputPath.toFile());

        task.generate(input, action);

        then(action).should().delete(new File("/test/File.java"), "File", outputPath);
        then(action).should().deleteDir(new File("/test/package"), outputPath.resolve("package"));
    }
}
