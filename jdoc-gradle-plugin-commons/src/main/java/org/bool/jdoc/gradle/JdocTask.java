package org.bool.jdoc.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileType;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.work.ChangeType;
import org.gradle.work.FileChange;
import org.gradle.work.Incremental;
import org.gradle.work.InputChanges;

import java.io.File;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import javax.inject.Inject;

public class JdocTask extends DefaultTask {

    private final Property<SourceDirectorySet> sources;

    private final Property<String> langTag;

    private final DirectoryProperty outputDir;

    @Inject
    public JdocTask(ObjectFactory objectFactory) {
        this(objectFactory.property(SourceDirectorySet.class), objectFactory.property(String.class), objectFactory.directoryProperty());
    }

    public JdocTask(Property<SourceDirectorySet> sources, Property<String> langTag, DirectoryProperty outputDir) {
        this.sources = sources;
        this.langTag = langTag;
        this.outputDir = outputDir;
    }

    @Incremental
    @InputFiles
    public Property<SourceDirectorySet> getSources() {
        return sources;
    }

    @Input
    public Property<String> getLangTag() {
        return langTag;
    }

    @OutputDirectory
    public DirectoryProperty getOutputDir() {
        return outputDir;
    }

    protected void generate(InputChanges changes, JdocAction handler) {
        Path outputRoot = outputDir.get().getAsFile().toPath();
        for (FileChange change : fileChanges(changes)) {
            if (change.getFileType() == FileType.FILE) {
                File file = change.getFile();
                String baseName = baseName(file);
                Path outputPath = outputRoot.resolve(relativizePath(file.toPath().getParent()));
                if (change.getChangeType() != ChangeType.ADDED) {
                    handler.delete(file, baseName, outputPath);
                }
                if (change.getChangeType() != ChangeType.REMOVED) {
                    handler.generate(file, baseName, outputPath);
                }
            } else if (change.getFileType() == FileType.DIRECTORY && change.getChangeType() == ChangeType.REMOVED) {
                handler.deleteDir(change.getFile(), outputRoot.resolve(relativizePath(change.getFile().toPath())));
            }
        }
    }

    protected String baseName(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        return index < 0 ? fileName : fileName.substring(0, index);
    }

    protected Iterable<FileChange> fileChanges(InputChanges changes) {
        return changes.getFileChanges((Provider) sources);
    }

    protected Path relativizePath(Path path) {
        return sources.get().getSrcDirs().stream()
            .map(File::toPath).filter(path::startsWith)
            .findAny().orElseThrow(() -> new NoSuchElementException("Path " + path + " not found in " + sources))
            .relativize(path);
    }
}
