# JUnit engine setup

Use this when the target project wants JUnit Platform to parse Java source during test discovery rather than generate test sources with a Gradle plugin. The upstream project uses its own engines this way: suites under `src/test/java` select `src/main/java`, where production classes hold executable blocks.

For Gradle, configure the existing `test` task with `useJUnitPlatform()` and select the source directory or individual Java file from a suite. The examples assume the project already manages JUnit versions through a compatible JUnit BOM or explicit dependency constraints; the unversioned suite and launcher dependencies require that management. `junit-platform-suite` includes both the suite API and its runtime engine. Keep jdoc-test on the test runtime classpath. Examples use `0.12.0`; preserve or select a compatible release for the target project.

For Maven or another JUnit Platform runner, use the same artifacts and selectors with that build tool's test dependency scopes and Platform support. Gradle plugins are not required for the engine approach.

## Spock engine

```groovy
dependencies {
    testImplementation 'org.junit.platform:junit-platform-suite'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    testRuntimeOnly 'io.github.boolivar.jdoctest:jdoc-spock:0.12.0'
}

test { useJUnitPlatform() }
```

```java
@Suite
@IncludeEngines("jdoc-spock")
@SelectDirectories("src/main/java")
public class JdocSpockTests {}
```

Import the three annotations from `org.junit.platform.suite.api`. `@SelectFile` is also supported. Alternatively provide comma-separated `jdoc.spock.test-dirs` or `jdoc.spock.test-files` as JUnit Platform configuration parameters. For Groovy compilation that needs external types, set `jdoc.spock.classpath` to comma-separated test compile classpath entries (not the OS path separator). `jdoc.spock.generated-classes-dir` selects the output directory. Compile production Java with `-parameters` to preserve constructor parameter names used by generated mock fields. See the [constructor rules](gradle-plugins.md#spock).

## Source behavior

`JavaFileParser` scans Javadocs and ordinary block comments and extracts matching top-level `<code lang="...">` blocks. Spock generation uses the Java file's primary type, package, and imports, and combines its Spock blocks into one specification. Avoid assuming a test under `src/main/java` will be discovered merely because the engine dependency exists: select source paths through a suite or platform configuration.
