package org.bool.jdoc.cucumber.gradle;

import org.gradle.api.tasks.JavaExec;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
class JdocCucumberPluginTest {

    @TempDir
    private static File temp;

    @BeforeAll
    void setup() throws IOException {
        Files.writeString(temp.toPath().resolve("build.gradle"), """
                plugins {
                    id "java"
                    id "io.github.boolivar.jdoctest.jdoc-cucumber"
                }
                repositories {
                    mavenCentral()
                }
                """);
        Files.createDirectories(temp.toPath().resolve("src/main/java/org/bool/jdoc/cucumber"));
        Files.writeString(temp.toPath().resolve("src/main/java/org/bool/jdoc/cucumber/Test.java"), """
                package org.bool.jdoc.cucumber;
                /**
                 * <pre><code lang="gherkin">
                 * TEST SPEC
                 * </code></pre>
                 */
                public class Test {
                    public static void main(String[] args) {
                    }
                }
                """);
    }

    @Test
    void testPlugin() {
        var project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply(JdocCucumberPlugin.ID);

        assertThat(project.getExtensions().getByName("jdocCucumber"))
            .isInstanceOf(JdocCucumberExtension.class);
        assertThat(project.getTasks().getByName("generateCucumberFeatures"))
            .isInstanceOf(JdocCucumberTask.class);
        assertThat(project.getTasks().getByName("jdocCucumberTest"))
            .isInstanceOf(JavaExec.class);
    }

    @Test
    void testBuild() {
        var result = GradleRunner.create()
                .withProjectDir(temp)
                .withPluginClasspath()
                .withArguments("generateCucumberFeatures")
                .build();
        assertThat(result.task(":generateCucumberFeatures").getOutcome())
            .isEqualTo(TaskOutcome.SUCCESS);
        assertThat(temp.toPath().resolve("build/generated/sources/jdoc-cucumber/org/bool/jdoc/cucumber/Test_1.feature"))
            .content().isEqualToIgnoringNewLines(" TEST SPEC ");
    }
}
