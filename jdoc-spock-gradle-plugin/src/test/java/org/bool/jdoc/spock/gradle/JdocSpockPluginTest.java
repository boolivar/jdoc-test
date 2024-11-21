package org.bool.jdoc.spock.gradle;

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
class JdocSpockPluginTest {

    @TempDir
    private static File temp;

    @BeforeAll
    void setup() throws IOException {
        Files.writeString(temp.toPath().resolve("build.gradle"), """
                plugins {
                    id "java"
                    id "io.github.boolivar.jdoctest.jdoc-spock"
                }
                repositories {
                    mavenCentral()
                }
                """);
        Files.createDirectories(temp.toPath().resolve("src/main/java/org/bool/jdoc/spock"));
        Files.writeString(temp.toPath().resolve("src/main/java/org/bool/jdoc/spock/Test.java"), """
                package org.bool.jdoc.spock;
                /**
                 * <pre><code lang="spock">
                 * def "jdoc spock plugin test"() {
                 *   expect:
                 *     Math.max(1, 2) == 2
                 * }
                 * </code></pre>
                 */
                public class Test {
                }
                """);
    }

    @Test
    void testPlugin() {
        var project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply(JdocSpockPlugin.ID);

        assertThat(project.getPluginManager().hasPlugin("groovy"))
            .isTrue();
        assertThat(project.getExtensions().getByName(JdocSpockPlugin.EXTENSION_NAME))
            .isInstanceOf(JdocSpockExtension.class);
        assertThat(project.getTasks().getByName(JdocSpockPlugin.GENERATE_SPECS_TASK_NAME))
            .isInstanceOf(JdocSpockTask.class);
        assertThat(project.getTasks().getByName(JdocSpockPlugin.TEST_TASK_NAME))
            .isInstanceOf(org.gradle.api.tasks.testing.Test.class);
    }

    @Test
    void testBuild() {
        var result = GradleRunner.create()
                .withProjectDir(temp)
                .withPluginClasspath()
                .withArguments("--configuration-cache", "jdocSpockTest")
                .build();
        assertThat(result.task(":jdocSpockTest").getOutcome())
            .isEqualTo(TaskOutcome.SUCCESS);
        assertThat(temp.toPath().resolve("build/generated/sources/jdoc-spock/org/bool/jdoc/spock/TestTestSpec.groovy"))
            .content().contains("def \"jdoc spock plugin test\"()");
    }
}
