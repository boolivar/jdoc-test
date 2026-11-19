# Cucumber examples

These complete files expand the delegation example in the [jdoc-cucumber README](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-cucumber/README.md) and the arithmetic example in the [root README](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/README.md). Use the target project's package names in place of `example.cucumber`.

Gherkin describes the class's behavior; Java glue performs the calls and assertions. jdoc-test extracts features, but does not supply `$target`, constructor mocks, or step definitions. Keep each feature in the declaring class's Javadoc and its glue in test sources. A feature block must be complete, even when several methods or classes have their own blocks.

## Delegation with an explicitly constructed subject

`src/main/java/example/cucumber/Bar.java`:

```java
package example.cucumber;

public interface Bar {
    void bar();
}
```

`src/main/java/example/cucumber/Foo.java`:

```java
package example.cucumber;

/**
 * Invokes a collaborator.
 *
 * <pre><code lang="gherkin">
 * Feature: Foo delegates to Bar
 *   Scenario: invoke foo
 *     When I invoke foo
 *     Then bar is invoked once
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

`src/test/java/example/cucumber/steps/FooSteps.java`:

```java
package example.cucumber.steps;

import example.cucumber.Bar;
import example.cucumber.Foo;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FooSteps {
    private final Bar bar = mock(Bar.class);
    private final Foo foo = new Foo(bar);

    @When("I invoke foo")
    public void invokeFoo() {
        foo.foo();
    }

    @Then("bar is invoked once")
    public void verifyBarInvoked() {
        verify(bar).bar();
    }
}
```

This glue requires `cucumber-java` and Mockito (`org.mockito:mockito-core`) on the test compile classpath. `verify(bar).bar()` asserts exactly one invocation. Mockito is a fixture choice, not a jdoc-test requirement. The subject and mock are instance fields in the glue; do not share mutable scenario state through static fields.

## Scenario outline with input and expected output

`src/main/java/example/cucumber/MathUtils.java`:

```java
package example.cucumber;

public final class MathUtils {
    private MathUtils() {}

    /**
     * Returns the square of the input.
     *
     * <pre><code lang="gherkin">
     * Feature: Square calculation
     *   Scenario Outline: square an integer
     *     When I square <input>
     *     Then the result is <expected>
     *     Examples:
     *       | input | expected |
     *       | 0     | 0        |
     *       | 2     | 4        |
     *       | -3    | 9        |
     * </code></pre>
     */
    public static int square(int value) {
        return value * value;
    }
}
```

`src/test/java/example/cucumber/steps/SquareSteps.java`:

```java
package example.cucumber.steps;

import example.cucumber.MathUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SquareSteps {
    private int result;

    @When("I square {int}")
    public void square(int input) {
        result = MathUtils.square(input);
    }

    @Then("the result is {int}")
    public void verifyResult(int expected) {
        if (result != expected) {
            throw new AssertionError("Expected " + expected + " but was " + result);
        }
    }
}
```

Each outline row becomes a scenario. `<input>` and `<expected>` are substituted by Gherkin; `{int}` in the annotations converts the resulting text into Java arguments. This example uses `AssertionError` to avoid another dependency; use the target project's assertion library when available. Do not use bare Java `assert` unless the runner enables assertions.

## Connect the production Javadocs and glue

Choose one execution integration for these examples:

- **Gradle plugin:** follow the [Cucumber plugin setup](gradle-plugins.md#cucumber), set `gluePackages` to `['example.cucumber.steps']`, and run `jdocCucumberTest`. It generates the features and invokes the Cucumber CLI with the test runtime classpath. No JUnit suite is needed for this path.
- **JUnit engine:** follow the [engine dependency setup](junit-engines.md#cucumber-engine) and add `src/test/java/example/cucumber/JdocCucumberTests.java` below. Run the configured JUnit Platform test task.

```java
package example.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("jdoc-cucumber")
@SelectDirectories("src/main/java")
@ConfigurationParameter(key = "cucumber.glue", value = "example.cucumber.steps")
public class JdocCucumberTests {}
```

Source selection locates the Java comments; glue configuration locates the step implementations. Both are necessary. The literal `cucumber.glue` key avoids a compile-time import from the engine dependency when it is runtime-only. Check that execution reports one delegation scenario and three arithmetic scenarios, with no undefined or ambiguous steps. Add an assertion library or Mockito only for examples that use it.

## Library source reference: configuration tables

For custom parameter types and data tables, read these files together:

- [ConfigParams](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-cucumber/src/main/java/org/bool/jdoc/cucumber/ConfigParams.java): the full declaring class with its Gherkin feature.
- [ConfigParamsStepdefs](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-cucumber/src/test/java/org/bool/jdoc/cucumber/glue/ConfigParamsStepdefs.java): `@ParameterType` resolves names to the class's constants, and a Then step accepts a `List<String>` from a one-column table.
- [JdocCucumberTestSuite](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-cucumber/src/test/java/org/bool/jdoc/cucumber/glue/JdocCucumberTestSuite.java): source and glue discovery.

The links identify the source revision used to prepare these examples. The installed skill's examples above can be read without a checkout or network access.
