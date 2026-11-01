package org.bool.jdoc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface JdocTestPlugin extends Plugin<Project> {

    String GROUP_ID = "io.github.boolivar.jdoctest";

    String TASK_GROUP = "jdoc-test";

    @Override
    void apply(Project project);

    default Properties loadProperties(Project project, String path) {
        Properties properties = new Properties();
        try (InputStream in = getClass().getResourceAsStream(path)) {
            properties.load(in);
        } catch (IOException e) {
            project.getLogger().error("Failed to load properties from: " + path, e);
        }
        return properties;
    }
}
