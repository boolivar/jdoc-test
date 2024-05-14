package org.bool.jdoc.spock.gradle;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.provider.Property;

public interface JdocSpockExtension {

    DirectoryProperty getOutputDir();

    Property<String> getLangTag();

    Property<SourceDirectorySet> getSources();

    Property<FileCollection> getClassPath();

    Property<String> getSpockVersion();

    Property<String> getByteBuddyVersion();

    Property<String> getObjenesisVersion();

}
