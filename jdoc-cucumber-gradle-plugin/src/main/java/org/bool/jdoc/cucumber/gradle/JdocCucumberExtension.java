package org.bool.jdoc.cucumber.gradle;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public interface JdocCucumberExtension {

    DirectoryProperty getOutputDir();

    Property<String> getLangTag();

    Property<FileCollection> getSources();

    Property<String> getCucumberVersion();

    ListProperty<String> getGluePackages();
}
