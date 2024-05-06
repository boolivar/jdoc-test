# jdoc-test

[![CI](https://github.com/boolivar/jdoc-test/workflows/CI/badge.svg)](https://github.com/boolivar/jdoc-test/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/boolivar/jdoc-test/graph/badge.svg?token=PCV1VTNFYT)](https://codecov.io/gh/boolivar/jdoc-test)
[![release](https://img.shields.io/github/v/release/boolivar/jdoc-test)](https://github.com/boolivar/jdoc-test/releases/latest)
[![](https://jitpack.io/v/boolivar/jdoc-test.svg)](https://jitpack.io/#boolivar/jdoc-test)

Write BDD tests in javadocs.

## What?

**jdoc-test** is a framework for javadoc sourced java tests.

Javadoc writing is cumbersome. Documentation quickly becomes outdated. There is no guarantee that code does what documentation says.  
Developers often prefer to write tests instead of documentation. Tests never lie.

So why not just write tests in documentation? [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) frameworks
use test specifications written in (more or less) human language. Such documentation goes in sync with actual code and shows code
usage example. Java code, tests and documentation become tightly coupled by putting BDD specification in javadoc.

## WHAT?

[jdoc-spock](#jdoc-spock) library runs [spockframework](https://spockframework.org/) test specifications from javadocs.

[jdoc-cucumber](#jdoc-cucumber) library supports [gherkin](https://cucumber.io/docs/gherkin/reference/) features written in javadocs.

[jdoc-cucumber-gradle-plugin](#jdoc-cucumber-gradle-plugin) gradle [plugin](https://plugins.gradle.org/plugin/org.bool.jdoctest.jdoc-cucumber) that automates cucumber feature generation and testing. 

:warning: **Library tests itself using itself executing own `jdoc-spock` tests written in javadocs.**

## WHAT???

Yes, see `jdoc-spock` and `jdoc-cucumber` test examples in source code.

## How?

#### jdoc-spock

---

1. Write `jdoc-spock` tests.
   
`jdoc-spock` contains junit platform engine to run tests. It consider text in javadoc or block comment between `<code lang="spock">` `</code>` tags as spock specification code.
Additional non-mandatory `<pre>` tag keeps code formatting for javadoc presentation:
```java
/**
 * <pre><code lang="spock">
 * def "Calling delegate bar method"() {
 *   when:
 *     $target.foo()
 *   then:
 *     1 * delegate.bar()
 * }
 * </code></pre>
 */
public void foo() {
    delegate.bar();
} 
```

2. Add `jdoc-spock` dependency.

`build.gradle` example:
```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    testRuntimeOnly "com.github.boolivar.jdoc-test:jdoc-spock:0.7.0"
}
```

Currently `jdoc-spock` available only on [jitpack](https://jitpack.io/#boolivar/jdoc-test).

3. Compile java code with parameter names using `javac` `-parameters` argument.

`build.gradle` example:
```gradle
compileJava {
    options.compilerArgs << "-parameters"
}
```

`jdoc-spock` uses constructor argument names to generate fields in specification initialized with mocks.
`$target` field of spock specification is initialized with instance of class under test (instance of primary class in java file where jdoc-spock specification is located).
`jdoc-spock` searches for biggest constructor with mockable (non-final class) arguments and creates mock for each constructor argument. Mocks stored in spec fields using corresponding names.

As an example for java class:

```java
public class Foo {
  private final Bar delegate;
  public Foo(Bar delegate) {
    this.delegate = delegate;
  }
}
```

generated spock fields will be:
```groovy
def delegate = Mock(Bar)
def $target = new Foo(delegate)
```

4. Set up paths to java sources using test suite `@SelectDirectories` or `@SelectFile`:

```java
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("jdoc-spock")
@SelectDirectories("src/main/java")
public class JdocSpockTestSuite {
}
```

jdoc-spock supports platform engine `DiscoverySelector` and `FileSelector`.

Optionally comma-separated paths to java sources can be provided using either `jdoc.spock.test-dirs` or `jdoc.spock.test-files` junit platform [Configuration Parameters](https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params).

`build.gradle` example:
```gradle
test {
    useJUnitPlatform()
    systemProperties = ["jdoc.spock.test-dirs" : sourceSets.main.java.srcDirs.join(",")]
}
```

5. Run tests with `jdoc-spock` junit engine.

`gradle` example:
```sh
gradle test
```

#### jdoc-cucumber

---

1. Write jdoc gherkin feature using `<code lang="gherkin">` tag:

```java
/**
 * <pre><code lang="gherkin">
 * Feature: foo() invokes bar()
 *   Scenario: invoke foo()
 *     When invoke foo()
 *     Then bar() invoked
 * </code></pre>
 */
public class Foo {

    private final Bar bar;

    public Foo(Bar bar) {
        this.bar = bar;
    }

    public void foo() {
        bar.bar();
    }
}
```

2. Provide cucumber and jdoc-cucumber test dependencies.

`build.gradle` example:
```gradle
dependencies {
    testRuntimeOnly "com.github.boolivar.jdoc-test:jdoc-cucumber:0.7.0"
    testImplementation "io.cucumber:cucumber-java:7.17.0"
}
```

3. Write cucumber step definitions.

```java
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.BDDMockito.*;

public class StepDefinitions {

    private final Bar bar = mock(Bar.class);

    private final Foo foo = new Foo(bar);

    @When("invoke foo()")
    public void invokeFoo() {
        foo.foo();
    }

    @Then("bar() invoked")
    public void verifyBarInvoked() {
        then(bar).should().bar();
    }
}
```

4. Set up paths to java sources using test suite `@SelectDirectories` or `@SelectFile` and step definitions package
using `io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME` configuration parameter:

```java
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("jdoc-cucumber")
@SelectDirectories("src/main/java")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "step.definitions.package")
public class JdocCucumberTestSuite {
}
```

5. Run tests with `jdoc-cucumber` junit engine.

`gradle` example:
```sh
gradle test
```

#### jdoc-cucumber-gradle-plugin

---

Gradle plugin [**not yet**] available on [gradle plugin portal](https://plugins.gradle.org/plugin/io.github.boolivar.jdoctest.jdoc-cucumber) that automates cucumber feature generation and cucumber testing tasks.

##### Minimal configuration:
`build.gradle`:
```gradle
plugins {
    id "java"
    id "io.github.boolivar.jdoctest.jdoc-cucumber" version "0.7.0"
}

repositories {
    mavenCentral()
}

check.dependsOn jdocCucumberTest
```

```sh
gradle check
```

##### jdocCucumber extension
`build.gradle` example:
```gradle
jdocCucumber {
    gluePackages = ["org.bool.cucumber.stepdefs"]
    cucumberVersion = "7.17.0"
    sourceSets.custom.java
}
```

| Extension property | Type | Default value | Description |
| ------------------ | ---- | ------------- | ----------- |
| `outputDir` | `Directory` | project.layout.buildDirectory.dir("generated/sources/jdoc-cucumber") | Path to store generated features |
| `langTag` | `String` | "gherkin" | `lang` tag to parse. Only `<code lang="<langTag>">` javadoc blocks will be parsed and written as features |
| `sources` | `SourceDirectorySet` | sourceSets.main.java | Java sources to parse |
| `cucumberVersion` | `String` | "7.17.0" | `io.cucumber:cucumber-java` dependecny version to register in `testImplementation` configuration |
| `gluePackages` | `List<String>` | | List of packages with cucumber glue code |

##### Tasks
When `java` plugin is applied to a project, `jdoc-cucumber` plugin registers `io.cucumber:cucumber-java` dependency in `testImplementation` configuration and creates 2 tasks:
- **generateCucumberFeatures** - `JdocCucumberTask`  
  Generates cucumber features from javadocs and stores them in `jdocCucumber.outputDir` path.
- **jdocCucumberTest** - `JavaExec`  
  _Depends on:_ all tasks with type `JdocCucumberTask`. Runs cucumber tests using cucumber CLI Runner. 

Note that by default `jdocCucumberTest` task is **not a dependency** for `check` task. To include `jdocCucumberTest` in build this should be configured manually.

`build.gradle` example:
```gradle
check.dependsOn jdocCucumberTest
```
