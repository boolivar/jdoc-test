package org.bool.jdoc.cucumber.gradle;

import org.bool.jdoc.gradle.JdocTask;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.InputChanges;

import javax.inject.Inject;

public class JdocCucumberTask extends JdocTask {

    @Inject
    public JdocCucumberTask(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    public JdocCucumberTask(Property<FileCollection> sources, Property<String> langTag, DirectoryProperty outputDir) {
        super(sources, langTag, outputDir);
    }

    @TaskAction
    public void generateFeatures(InputChanges changes) {
        generate(changes, new JdocCucumberAction(getLangTag().get()));
    }
}
