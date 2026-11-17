# Gradle plugin setup

Use this when adding or editing the generated-source Gradle integration. Apply `io.github.boolivar.jdoctest.jdoc-spock` in each Java subproject that owns these Javadoc tests.

Examples use release `0.12.0`; select versions according to the target project's compatibility requirements. Plugin resolution uses Gradle's plugin repositories; ordinary dependencies need `mavenCentral()` or the project's equivalent dependency repository. The integration requires the Java plugin (also applied by `java-library`) before its tasks are registered.

## Spock

Groovy DSL example:

```groovy
plugins {
    id 'java'
    id 'io.github.boolivar.jdoctest.jdoc-spock' version '0.12.0'
}

repositories { mavenCentral() }

dependencies {
    jdocSpockRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

// Optional: include Javadoc tests in the normal verification lifecycle.
tasks.named('check') { dependsOn tasks.named('jdocSpockTest') }
```

Kotlin DSL equivalent for the lifecycle link: `tasks.named("check") { dependsOn("jdocSpockTest") }`.

Declare the launcher on `jdocSpockRuntimeOnly`: Gradle 9 no longer supplies it automatically. Adding it only to `testRuntimeOnly` does not put it on this custom source set's runtime classpath. Keep its version aligned with the resolved Spock/JUnit Platform dependencies, using their constraints or the project's compatible JUnit BOM. Kotlin DSL: `add("jdocSpockRuntimeOnly", "org.junit.platform:junit-platform-launcher")`. See [Gradle's test framework dependency guidance](https://docs.gradle.org/current/userguide/upgrading_version_8.html#test_framework_implementation_dependencies).

The plugin applies `groovy`, creates the `jdocSpock` source set and `generateSpockSpecs`, `compileJdocSpockGroovy`, and `jdocSpockTest` tasks. The generation task depends on `compileJava`; compilation of the generated Groovy depends on generation; `jdocSpockTest` uses JUnit Platform. It adds `spock-core` to `jdocSpockImplementation` and Byte Buddy and Objenesis to `jdocSpockRuntimeOnly`. It does **not** attach `jdocSpockTest` to `check` automatically.

Optional `jdocSpock` extension properties: `sources` (main Java source directories by default), `outputDir` (`build/generated/sources/jdoc-spock`), `langTag` (`spock`), `classPath` (generated source set compile classpath), `spockVersion`, `byteBuddyVersion`, and `objenesisVersion`. Configure only what the project needs. Check the applied plugin's defaults before overriding dependencies, and keep the Spock artifact's Groovy variant compatible with the resolved Groovy version.

For generated `$target` and mock field names, compile the Java production sources with `-parameters`:

```groovy
tasks.named('compileJava', JavaCompile) {
    options.compilerArgs += ['-parameters']
}
```

The generator chooses the unique constructor with the most parameters whose parameter types are non-final; private constructors are excluded. A non-private zero-argument constructor qualifies. If no constructor qualifies, `$target` is absent. If multiple eligible constructors tie for the largest parameter count, generation fails with an ambiguity. Directly constructing the subject in a block can replace an absent `$target`, but does not bypass constructor ambiguity during generation.

## Build conventions

In a version catalog, declare plugin aliases and versions there instead of copying literal plugin declarations into each module. In a precompiled convention plugin, add the plugin marker artifact to the convention build's dependencies, then apply the plugin ID without a version inside the precompiled script. Preserve an existing centralized `check` policy; the snippets above are examples when automatic lifecycle inclusion is wanted.
