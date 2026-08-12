# jdoc-cucumber

> Part of [jdoc-test](../README.md). Runs [gherkin](https://cucumber.io/docs/gherkin/reference/) features written in javadocs.

<!-- x-release-please-start-version -->
```xml
<dependency>
    <groupId>io.github.boolivar.jdoctest</groupId>
    <artifactId>jdoc-cucumber</artifactId>
    <version>0.12.0</version>
    <scope>test</scope>
</dependency>
```
<!-- x-release-please-end -->

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
<pre>
repositories {
    mavenCentral()
}

dependencies {
    <!-- x-release-please-version -->testRuntimeOnly "io.github.boolivar.jdoctest:jdoc-cucumber:0.12.0"
    testImplementation "io.cucumber:cucumber-java:7.17.0"
}
</pre>

> [!IMPORTANT]
> `jdoc-cucumber` versions before `0.9.0` available only on [jitpack](https://jitpack.io/#boolivar/jdoc-test).
>
> `build.gradle` example:
> ```gradle
> repositories {
>     maven { url "https://jitpack.io" }
> }
> dependencies {
>     testRuntimeOnly "io.github.boolivar.jdoctest:jdoc-cucumber:0.8.1"
>     testImplementation "io.cucumber:cucumber-java:7.17.0"
> }
> ```

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
