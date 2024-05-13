package org.bool.jdoc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public interface JdocTestPlugin extends Plugin<Project> {

    String GROUP_ID = "io.github.boolivar.jdoctest";

    String TASK_GROUP = "jdoc-test";

    @Override
    void apply(Project project);
}
