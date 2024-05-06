package org.bool.jdoc.gradle.spock;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.provider.Property;

public interface JdocSpockExtension {

    DirectoryProperty getOutputDir();

    Property<String> getLangTag();

    Property<SourceDirectorySet> getSources();

    Property<String> getSpockVersion();

    Property<String> getGroovyVersion();

    Property<String> getByteBuddyVersion();

    Property<String> getObjenesisVersion();

}
