# Gradle plugin setup

Use this when adding or editing the generated-source Gradle integration. Apply `io.github.boolivar.jdoctest.jdoc-cucumber` in each Java subproject that owns these Javadoc tests.

Examples use release `0.12.0`; select versions according to the target project's compatibility requirements. Plugin resolution uses Gradle's plugin repositories; ordinary dependencies need `mavenCentral()` or the project's equivalent dependency repository. The integration requires the Java plugin (also applied by `java-library`) before its tasks are registered.

## Cucumber

Groovy DSL example:

```groovy
plugins {
    id 'java'
    id 'io.github.boolivar.jdoctest.jdoc-cucumber' version '0.12.0'
}

repositories { mavenCentral() }

jdocCucumber {
    gluePackages = ['com.example.steps']
}

// Optional: include Javadoc tests in the normal verification lifecycle.
tasks.named('check') { dependsOn tasks.named('jdocCucumberTest') }
```

Kotlin DSL equivalent for glue: `jdocCucumber { gluePackages.set(listOf("com.example.steps")) }`.

The plugin creates `generateCucumberFeatures` and `jdocCucumberTest`. The latter is a `JavaExec` task invoking Cucumber CLI with the generated feature directory and `--glue` packages on the `test` runtime classpath. The plugin adds `cucumber-java` to `testImplementation`; put step definitions under the test source set and add any assertion or mocking dependencies they use. Set `gluePackages` to the packages containing the project's step definitions so the CLI receives the intended glue scope. The plugin does **not** attach `jdocCucumberTest` to `check` automatically.

Optional `jdocCucumber` properties: `sources` (main Java source directories), `outputDir` (`build/generated/sources/jdoc-cucumber`), `langTag` (`gherkin`), and `cucumberVersion`. Align any additional Cucumber dependencies with the selected version. A separate Gherkin code block generates a separate `.feature` file.

## Build conventions

In a version catalog, declare plugin aliases and versions there instead of copying literal plugin declarations into each module. In a precompiled convention plugin, add the plugin marker artifact to the convention build's dependencies, then apply the plugin ID without a version inside the precompiled script. Preserve an existing centralized `check` policy; the snippets above are examples when automatic lifecycle inclusion is wanted.
