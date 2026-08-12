# jdoc-cucumber-gradle-plugin

> Part of [jdoc-test](../README.md). Gradle plugin available on [gradle plugin portal](https://plugins.gradle.org/plugin/io.github.boolivar.jdoctest.jdoc-cucumber) that automates cucumber feature generation and cucumber testing tasks.

## Minimal configuration:
`build.gradle`:
<!-- x-release-please-start-version -->
```gradle
plugins {
    id "java"
    id "io.github.boolivar.jdoctest.jdoc-cucumber" version "0.12.0"
}

repositories {
    mavenCentral()
}

check.dependsOn jdocCucumberTest
```
<!-- x-release-please-end -->

```sh
gradle check
```

## jdocCucumber extension
`build.gradle` example:
```gradle
jdocCucumber {
    gluePackages = ["org.bool.cucumber.stepdefs"]
    cucumberVersion = "7.18.1"
    sources = sourceSets.custom.java
}
```

| Extension property | Type | Default value | Description |
| ------------------ | ---- | ------------- | ----------- |
| `outputDir` | `Directory` | project.layout.buildDirectory.dir("generated/sources/jdoc-cucumber") | Path to store generated features |
| `langTag` | `String` | "gherkin" | `lang` tag to parse. Only `<code lang="<langTag>">` javadoc blocks will be parsed and written as features |
| `sources` | `FileCollection` | sourceSets.main.java.sourceDirectories | source directories to search java files with jdoc-cucumber comments |
| `cucumberVersion` | `String` | "7.17.0" | `io.cucumber:cucumber-java` dependency version to register in `testImplementation` configuration |
| `gluePackages` | `List<String>` | | List of packages with cucumber glue code |

## Tasks
When `java` plugin is applied to a project, `jdoc-cucumber` plugin registers `io.cucumber:cucumber-java` dependency in `testImplementation` configuration and creates 2 tasks:
- **generateCucumberFeatures** - `JdocCucumberTask`  
  Generates cucumber features from javadocs and stores them in `jdocCucumber.outputDir` path.
- **jdocCucumberTest** - `JavaExec`  
  _Depends on:_ all tasks with type `JdocCucumberTask`. Runs cucumber tests using cucumber CLI Runner. 

> [!NOTE]
> By default `jdocCucumberTest` task is **not a dependency** for `check` task. To include `jdocCucumberTest` in build this should be configured manually.
>
> `build.gradle` example:
> ```gradle
> check.dependsOn jdocCucumberTest
> ```
