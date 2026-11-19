# JUnit engine setup

Use this when the target project wants JUnit Platform to parse Java source during test discovery rather than generate test sources with a Gradle plugin. The upstream project uses its own engines this way: suites under `src/test/java` select `src/main/java`, where production classes hold executable blocks.

For Gradle, configure the existing `test` task with `useJUnitPlatform()` and select the source directory or individual Java file from a suite. The examples assume the project already manages JUnit versions through a compatible JUnit BOM or explicit dependency constraints; the unversioned suite and launcher dependencies require that management. `junit-platform-suite` includes both the suite API and its runtime engine. Keep jdoc-test on the test runtime classpath. Examples use `0.12.0`; preserve or select a compatible release for the target project.

For Maven or another JUnit Platform runner, use the same artifacts and selectors with that build tool's test dependency scopes and Platform support. Gradle plugins are not required for the engine approach.

## Cucumber engine

```groovy
dependencies {
    testImplementation 'org.junit.platform:junit-platform-suite'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    testImplementation "io.cucumber:cucumber-java:${cucumberVersion}"
    testRuntimeOnly 'io.github.boolivar.jdoctest:jdoc-cucumber:0.12.0'
}

test { useJUnitPlatform() }
```

```java
@Suite
@IncludeEngines("jdoc-cucumber")
@SelectDirectories("src/main/java")
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.steps")
public class JdocCucumberTests {}
```

Define `cucumberVersion` using the project's version management and align it with the Cucumber engine dependency of the selected jdoc-test release. Import the annotations from `org.junit.platform.suite.api`. The literal `cucumber.glue` key avoids importing Cucumber engine constants from a runtime-only dependency. Add matching step definitions under the test source set and any assertion or mocking libraries they use. `@SelectFile` and comma-separated `jdoc.cucumber.test-dirs` / `jdoc.cucumber.test-files` are also supported. The engine writes features to a temporary directory by default; `jdoc.cucumber.output-dir` overrides that location.

## Source behavior

`JavaFileParser` scans Javadocs and ordinary block comments and extracts matching top-level `<code lang="...">` blocks. Cucumber creates feature files for each Gherkin block; each block should be a complete feature. Avoid assuming a test under `src/main/java` will be discovered merely because the engine dependency exists: select source paths through a suite or platform configuration.
