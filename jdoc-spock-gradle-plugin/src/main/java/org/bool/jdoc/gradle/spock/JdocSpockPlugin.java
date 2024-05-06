package org.bool.jdoc.gradle.spock;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Property;

public class JdocSpockPlugin implements Plugin<Project> {

    public static final String ID = "io.github.boolivar.jdoctest.jdoc-spock";

    public static final String TASK_GROUP = "jdoc-spock";

    public static final String GENERATE_SPOCK_SPECS_TASK_NAME = "generateSpockSpecs";

    @Override
    public void apply(Project project) {
        JdocSpockExtension extension = project.getExtensions().create("jdocCucumber", JdocSpockExtension.class);
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            configureExtension(project, extension);

            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.spockframework:spock-core", extension.getSpockVersion());
            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.apache.groovy:groovy", extension.getGroovyVersion());
            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "net.bytebuddy:byte-buddy", extension.getByteBuddyVersion());
            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.objenesis:objenesis", extension.getObjenesisVersion());

            project.getTasks().register(GENERATE_SPOCK_SPECS_TASK_NAME, JdocSpockTask.class, task -> configureSpockTask(task, extension));
        });
    }

    private void configureDependency(Project project, String configuration, String dependency, Property<String> version) {
        project.getDependencies().addProvider(configuration, version.filter(v -> !v.isEmpty()).map(v -> dependency + ":" + v));
    }

    private void configureExtension(Project project, JdocSpockExtension extension) {
        extension.getOutputDir().convention(project.getLayout().getBuildDirectory().dir("generated/sources/jdoc-cucumber"));
        extension.getLangTag().convention("spock");
        extension.getGroovyVersion().convention("2.3-groovy-4.0");
        extension.getByteBuddyVersion().convention("1.14.12");
        extension.getObjenesisVersion().convention("3.3");
        extension.getSources().convention(project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main").getJava());
    }

    private void configureSpockTask(JdocSpockTask task, JdocSpockExtension extension) {
        task.setGroup(TASK_GROUP);
        task.dependsOn(task.getProject().getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        task.setDescription("Generate spock specs");
        task.getOutputDir().set(extension.getOutputDir());
        task.getLangTag().set(extension.getLangTag());
        task.getSources().set(extension.getSources());
    }
}
