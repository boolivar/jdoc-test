package org.bool.jdoc.spock.gradle;

import org.bool.jdoc.gradle.JdocTask;
import org.bool.jdoc.spock.ResourceContainer;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.InputChanges;

import java.util.function.Function;

import javax.inject.Inject;

public class JdocSpockTask extends JdocTask {

    private final Property<FileCollection> classPath;

    private final Function<FileCollection, ResourceContainer<ClassLoader>> classLoaderFactory;

    @Inject
    public JdocSpockTask(ObjectFactory objectFactory) {
        super(objectFactory);
        this.classPath = objectFactory.property(FileCollection.class);
        this.classLoaderFactory = new UrlClassLoaderFactory()::createClassLoader;
    }

    public JdocSpockTask(Property<FileCollection> sources, Property<String> langTag, DirectoryProperty outputDir,
            Property<FileCollection> classPath, Function<FileCollection, ResourceContainer<ClassLoader>> classLoaderFactory) {
        super(sources, langTag, outputDir);
        this.classPath = classPath;
        this.classLoaderFactory = classLoaderFactory;
    }

    @Classpath
    @InputFiles
    public Property<FileCollection> getClassPath() {
        return classPath;
    }

    @TaskAction
    public void generateSpecs(InputChanges changes) {
        try (ResourceContainer<ClassLoader> classLoader = classLoaderFactory.apply(classPath.get())) {
            generate(changes, new JdocSpockAction(classLoader.getResource(), new SpecGenerator(getLangTag().get())));
        } catch (Exception e) {
            throw new RuntimeException("Spec generation error", e);
        }
    }
}
