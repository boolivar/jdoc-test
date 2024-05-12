package org.bool.jdoc.gradle.spock;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.GroovySourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.testing.Test;

import java.util.Locale;

public class JdocSpockPlugin implements Plugin<Project> {

    public static final String ID = "io.github.boolivar.jdoctest.jdoc-spock";

    public static final String TASK_GROUP = "jdoc-spock";

    public static final String EXTENSION_NAME = "jdocSpock";

    public static final String GENERATE_SPECS_TASK_NAME = "generateSpockSpecs";

    public static final String TEST_TASK_NAME = "jdocSpockTest";

    private static final String SOURCE_SET_NAME = "jdocSpock";

    @Override
    public void apply(Project project) {
        JdocSpockExtension extension = project.getExtensions().create(EXTENSION_NAME, JdocSpockExtension.class);
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            project.getPluginManager().apply("groovy");

            configureExtension(project, extension);
            configureSourceSet(project, extension);

            project.getTasks().register(GENERATE_SPECS_TASK_NAME, JdocSpockTask.class, task -> configureSpockTask(task, extension));
            project.getTasks().register(TEST_TASK_NAME, Test.class, this::configureTestTask);
            project.getTasks().named("compile" + SOURCE_SET_NAME.substring(0, 1).toUpperCase(Locale.ROOT) + SOURCE_SET_NAME.substring(1) + "Groovy")
                .configure(task -> task.dependsOn(GENERATE_SPECS_TASK_NAME));

            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.spockframework:spock-core", extension.getSpockVersion());
            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "net.bytebuddy:byte-buddy", extension.getByteBuddyVersion());
            configureDependency(project, JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.objenesis:objenesis", extension.getObjenesisVersion());
        });
    }

    private void configureExtension(Project project, JdocSpockExtension extension) {
        Provider<SourceSet> sources = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().named("main");
        extension.getOutputDir().convention(project.getLayout().getBuildDirectory().dir("generated/sources/jdoc-spock"));
        extension.getLangTag().convention("spock");
        extension.getSpockVersion().set("2.3-groovy-4.0");
        extension.getByteBuddyVersion().set("1.14.12");
        extension.getObjenesisVersion().set("3.3");
        extension.getSources().convention(sources.map(SourceSet::getJava));
        extension.getClassPath().convention(sources.map(SourceSet::getOutput));
    }

    private void configureSourceSet(Project project, JdocSpockExtension extension) {
        SourceSetContainer sourceSets = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
        sourceSets.create(SOURCE_SET_NAME, sourceSet -> {
            sourceSet.getExtensions().getByType(GroovySourceDirectorySet.class).srcDir(extension.getOutputDir());
            sourceSet.setCompileClasspath(sourceSet.getCompileClasspath().plus(sourceSets.getByName("main").getOutput()));
            sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath().plus(sourceSets.getByName("main").getOutput()));
        });
        project.getConfigurations().named(SOURCE_SET_NAME + "Implementation")
            .configure(cfg -> cfg.extendsFrom(project.getConfigurations().getByName(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME)));
    }

    private void configureSpockTask(JdocSpockTask task, JdocSpockExtension extension) {
        task.setGroup(TASK_GROUP);
        task.dependsOn(task.getProject().getTasks().named(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        task.setDescription("Generate spockframework specs");
        task.getOutputDir().set(extension.getOutputDir());
        task.getLangTag().set(extension.getLangTag());
        task.getSources().set(extension.getSources());
        task.getClassPath().set(extension.getClassPath());
    }

    private void configureTestTask(Test task) {
        SourceSet jdocSpockSourceSet = task.getProject().getExtensions().getByType(JavaPluginExtension.class)
            .getSourceSets().getByName(SOURCE_SET_NAME);
        task.setGroup(TASK_GROUP);
        task.setDescription("Run jdoc spock tests");
        task.setClasspath(jdocSpockSourceSet.getRuntimeClasspath());
        task.setTestClassesDirs(jdocSpockSourceSet.getOutput().getClassesDirs());
        task.useJUnitPlatform();
    }

    private void configureDependency(Project project, String configuration, String dependency, Property<String> version) {
        project.getDependencies().addProvider(configuration, version.map(v -> v.isEmpty() ? dependency : dependency + ":" + v));
    }
}
