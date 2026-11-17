# Gradle plugin setup

Use this when adding or editing the generated-source Gradle integration. Apply the plugin in each Java subproject that owns Javadoc tests. The plugin IDs are:

- `io.github.boolivar.jdoctest.jdoc-spock`
- `io.github.boolivar.jdoctest.jdoc-cucumber`

Examples use release `0.12.0`; select versions according to the target project's compatibility requirements. Plugin resolution uses Gradle's plugin repositories; ordinary dependencies need `mavenCentral()` or the project's equivalent dependency repository. Both integrations require the Java plugin (also applied by `java-library`) before their tasks are registered.

## Spock

Groovy DSL example:

```groovy
plugins {
    id 'java'
    id 'io.github.boolivar.jdoctest.jdoc-spock' version '0.12.0'
}

repositories { mavenCentral() }

// Optional: include Javadoc tests in the normal verification lifecycle.
tasks.named('check') { dependsOn tasks.named('jdocSpockTest') }
```

Kotlin DSL equivalent for the lifecycle link: `tasks.named("check") { dependsOn("jdocSpockTest") }`.

The plugin applies `groovy`, creates the `jdocSpock` source set and `generateSpockSpecs`, `compileJdocSpockGroovy`, and `jdocSpockTest` tasks. The generation task depends on `compileJava`; compilation of the generated Groovy depends on generation; `jdocSpockTest` uses JUnit Platform. It adds `spock-core` to `jdocSpockImplementation` and Byte Buddy and Objenesis to `jdocSpockRuntimeOnly`. It does **not** attach `jdocSpockTest` to `check` automatically.

Optional `jdocSpock` extension properties: `sources` (main Java source directories by default), `outputDir` (`build/generated/sources/jdoc-spock`), `langTag` (`spock`), `classPath` (generated source set compile classpath), `spockVersion`, `byteBuddyVersion`, and `objenesisVersion`. Configure only what the project needs. Check the applied plugin's defaults before overriding dependencies, and keep the Spock artifact's Groovy variant compatible with the resolved Groovy version.

For generated `$target` and mock field names, compile the Java production sources with `-parameters`:

```groovy
tasks.named('compileJava', JavaCompile) {
    options.compilerArgs += ['-parameters']
}
```

The generator chooses the unique constructor with the most parameters whose parameter types are non-final; private constructors are excluded. A non-private zero-argument constructor qualifies. If no constructor qualifies, `$target` is absent. If multiple eligible constructors tie for the largest parameter count, generation fails with an ambiguity. Directly constructing the subject in a block can replace an absent `$target`, but does not bypass constructor ambiguity during generation.

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
