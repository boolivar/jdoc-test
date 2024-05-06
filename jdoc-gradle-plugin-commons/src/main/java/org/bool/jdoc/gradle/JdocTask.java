package org.bool.jdoc.gradle;

import org.bool.jdoc.core.JavaFileParser;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.FileChange;
import org.gradle.work.Incremental;
import org.gradle.work.InputChanges;

import java.nio.file.Path;

import javax.inject.Inject;

public class JdocTask extends DefaultTask {

    private final Property<SourceDirectorySet> sources;

    private final Property<String> langTag;

    private final DirectoryProperty outputDir;

    private final JdocGenerateAction generateAction;

    @Inject
    public JdocTask(ObjectFactory objectFactory) {
        this(objectFactory.property(SourceDirectorySet.class), objectFactory.property(String.class), objectFactory.directoryProperty(), new JdocGenerateAction());
    }

    public JdocTask(Property<SourceDirectorySet> sources, Property<String> langTag, DirectoryProperty outputDir, JdocGenerateAction generateAction) {
        this.sources = sources;
        this.langTag = langTag;
        this.outputDir = outputDir;
        this.generateAction = generateAction;
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

    @TaskAction
    public void generate(InputChanges changes) {
        JavaFileParser parser = new JavaFileParser(langTag.get());
        Path outputPath = outputDir.get().getAsFile().toPath();
        Iterable<FileChange> fileChanges = changes.getFileChanges((Provider) sources);
        generateAction.generateFeatures(fileChanges, sources.get(), parser, outputPath);
    }
}
