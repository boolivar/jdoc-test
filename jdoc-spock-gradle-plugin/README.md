# jdoc-spock-gradle-plugin

> Part of [jdoc-test](../README.md). Gradle plugin available on [gradle plugin portal](https://plugins.gradle.org/plugin/io.github.boolivar.jdoctest.jdoc-spock) that automates spockframework specs generation and testing tasks.

## Configuration example
`build.gradle`:
<!-- x-release-please-start-version -->
```gradle
plugins {
    id "java"
    id "io.github.boolivar.jdoctest.jdoc-spock" version "0.12.0"
}

repositories {
    mavenCentral()
}

jdocSpockTest {
    testLogging {
        events "passed", "skipped", "failed"
    }
}

check.dependsOn jdocSpockTest
```
<!-- x-release-please-end -->

```sh
gradle check
```

## Reacting to the java plugin
When `java` plugin is applied to a project, `jdoc-spock` plugin:
- applies `groovy` plugin
- creates source set `jdocSpock` with groovy sources configured to outputDir property of extension
- registers `org.spockframework:spock-core` as implementation dependency for `jdocSpock` source set
- registers `net.bytebuddy:byte-buddy` and `org.objenesis:objenesis` as runtimeOnly dependencies for `jdocSpock` source set
- creates `generateSpockSpecs` task
- creates `jdocSpockTest` task
- configures `compileJdocSpockGroovy` task to depend on `generateSpockSpecs` task

## jdocSpock extension
`build.gradle` example:
```gradle
jdocSpock {
    outputDir = project.layout.buildDirectory.dir("spock-specs")
    spockVersion = "2.3-groovy-4.0"
    byteBuddyVersion = null
    objenesisVersion = null
}
```

| Extension property | Type | Default value | Description |
| ------------------ | ---- | ------------- | ----------- |
| `outputDir` | `Directory` | project.layout.buildDirectory.dir("generated/sources/jdoc-spock") | Path to store generated groovy specs |
| `langTag` | `String` | "spock" | `lang` tag to parse. Only `<code lang="<langTag>">` javadoc blocks will be parsed and included in spec generation |
| `sources` | `FileCollection` | sourceSets.main.java.sourceDirectories | source directories to search java files with jdoc-spock comments |
| `classPath` | `FileCollection` | sourceSets.jdocSpock.compileClasspath | Classpath for mockable constructor search during spec generation. |
| `spockVersion` | `String` | "2.3-groovy-4.0" | `org.spockframework:spock-core` dependency version to register in `jdocSpockImplementation` configuration |
| `byteBuddyVersion` | `String` | "1.14.15" | `net.bytebuddy:byte-buddy` dependency version to register in `jdocSpockRuntimeOnly` configuration, `null` value will exclude dependency. |
| `objenesisVersion` | `String` | "3.3" | `org.objenesis:objenesis` dependency version to register in `jdocSpockRuntimeOnly` configuration, `null` value will exclude dependency. |

## Tasks
- **generateSpockSpecs** - `JdocSpockTask`  
  _Depends on:_ `compileJava`. Generates spockframework test specs from javadocs and stores them in `jdocSpock.outputDir` path.
- **jdocSpockTest** - `Test`  
  Runs spockframework tests using junit platform . 

> [!NOTE]
> By default `jdocSpockTest` task is **not a dependency** for `check` task. To include `jdocSpockTest` in build this should be configured manually.
> 
> `build.gradle` example:
>
> ```gradle
> check.dependsOn jdocSpockTest
> ```
