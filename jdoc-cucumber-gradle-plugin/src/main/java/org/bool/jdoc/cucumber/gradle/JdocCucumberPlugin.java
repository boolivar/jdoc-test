package org.bool.jdoc.cucumber.gradle;

import org.bool.jdoc.gradle.JdocTestPlugin;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.JavaExec;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdocCucumberPlugin implements JdocTestPlugin {

    public static final String ID = GROUP_ID + ".jdoc-cucumber";

    public static final String GENERATE_FEATURES_TASK_NAME = "generateCucumberFeatures";

    public static final String TEST_TASK_NAME = "jdocCucumberTest";

    @Override
    public void apply(Project project) {
        JdocCucumberExtension extension = project.getExtensions().create("jdocCucumber", JdocCucumberExtension.class);
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            configureExtension(project, extension);
            project.getDependencies().addProvider(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, extension.getCucumberVersion().map(version -> "io.cucumber:cucumber-java:" + version));
            project.getTasks().register(GENERATE_FEATURES_TASK_NAME, JdocCucumberTask.class, task -> configureCucumberTask(task, extension));
            project.getTasks().register(TEST_TASK_NAME, JavaExec.class, task -> configureTestTask(task, extension));
        });
    }

    private void configureExtension(Project project, JdocCucumberExtension extension) {
        extension.getOutputDir().convention(project.getLayout().getBuildDirectory().dir("generated/sources/jdoc-cucumber"));
        extension.getLangTag().convention("gherkin");
        extension.getCucumberVersion().convention("7.17.0");
        extension.getSources().convention(project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main").getJava());
    }

    private void configureCucumberTask(JdocCucumberTask task, JdocCucumberExtension extension) {
        task.setGroup(TASK_GROUP);
        task.dependsOn(task.getProject().getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        task.setDescription("Generate cucumber features");
        task.getOutputDir().set(extension.getOutputDir());
        task.getLangTag().set(extension.getLangTag());
        task.getSources().set(extension.getSources());
    }

    private void configureTestTask(JavaExec task, JdocCucumberExtension extension) {
        task.setGroup(TASK_GROUP);
        task.setDescription("Run jdoc-cucumber tests using cucumber CLI Runner");
        task.dependsOn(task.getProject().getTasks().withType(JdocCucumberTask.class));
        task.getMainClass().set("io.cucumber.core.cli.Main");
        task.setClasspath(task.getProject().getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("test").getRuntimeClasspath());
        task.args(Stream.concat(
                Stream.of(extension.getOutputDir().get().toString()),
                extension.getGluePackages().get().stream().flatMap(gluePackage -> Stream.of("--glue", gluePackage))
            ).collect(Collectors.toList()));
    }
}
