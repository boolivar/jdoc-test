# Spock examples

Read the Java implementation and its executable Javadoc together. These examples expand the [jdoc-spock README](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-spock/README.md) into complete files for a consuming project. They do not require the library's internal classes or Lombok. Use the target project's package names in place of `example.spock`.

Choose the [Gradle plugin](gradle-plugins.md#spock) or [JUnit engine](junit-engines.md#spock-engine) setup already used by the project. All production files below go in `src/main/java/example/spock/`. Compile them with `-parameters` when using constructor mock names. Generated tests require both these compiled classes and their Java sources.

## Constructor injection: generated mocks and $target

`Bar.java`:

```java
package example.spock;

public interface Bar {
    void bar();
}
```

`Foo.java`:

```java
package example.spock;

public class Foo {
    private final Bar delegate;

    public Foo(Bar delegate) {
        this.delegate = delegate;
    }

    /**
     * Invokes the delegate once.
     *
     * <pre><code lang="spock">
     * def "calls the delegate"() {
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
}
```

jdoc-test generates a `FooTestSpec` in `example.spock`, extending Spock's `Specification`. Its fields are equivalent to the following; this is generated output, **not code to paste into the Javadoc**:

```groovy
def delegate = Mock(Bar)
def $target = new Foo(delegate)
```

The feature invokes the actual `Foo.foo()` implementation and verifies the call on the injected mock. `delegate` comes from the constructor parameter name, not from scanning Java field names. Missing `-parameters` can produce names such as `arg0` instead. A local replacement mock will not change the collaborator already held by `$target`.

For methods returning values, stub the generated collaborator using Spock's `>>`. If the same call needs both a response and a call count, combine them in one interaction, such as `1 * collaborator.lookup(key) >> value`, rather than defining competing stubs and verification interactions.

## No-argument subject: data tables and exceptions

`Divider.java`:

```java
package example.spock;

public class Divider {
    /**
     * Divides integers, truncating toward zero.
     *
     * <pre><code lang="spock">
     * def "divides #dividend by #divisor"() {
     *   expect:
     *     $target.divide(dividend, divisor) == quotient
     *   where:
     *     dividend | divisor || quotient
     *     8        | 2       || 4
     *     7        | 2       || 3
     *     -7       | 2       || -3
     * }
     *
     * def "rejects a zero divisor"() {
     *   when:
     *     $target.divide(1, 0)
     *   then:
     *     thrown(ArithmeticException)
     * }
     * </code></pre>
     *
     * @throws ArithmeticException if the divisor is zero
     */
    public int divide(int dividend, int divisor) {
        return dividend / divisor;
    }
}
```

Java supplies a public no-argument constructor, so jdoc-test initializes `$target` with `new Divider()`. The feature methods belong to the generated specification, not to `Divider`. Both methods in the block become tests; the data-driven method exercises each table row. Fields and fixture methods can also appear in a block, and all blocks from this Java file share the same generated specification.

## Constructor with a final parameter type: explicit subject

`Greeting.java`:

```java
package example.spock;

public class Greeting {
    private final String prefix;

    public Greeting(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Greets a person using the configured prefix.
     *
     * <pre><code lang="spock">
     * def "uses the configured prefix"() {
     *   given:
     *     def greeting = new Greeting("Hello")
     *   expect:
     *     greeting.greet("Ada") == "Hello, Ada"
     * }
     * </code></pre>
     */
    public String greet(String name) {
        return prefix + ", " + name;
    }
}
```

`String` is final, so this class's only constructor is ineligible for automatic mocking. No `$target` is generated: construct the subject explicitly with real input. A utility class with only a private constructor likewise has no generated target; call its static methods by class name. Explicit construction does not bypass an ambiguity between eligible constructors, because constructor selection happens before feature execution. Do not change a production API merely to make an example use `$target`.

## Connect these files to execution

With the Gradle plugin, run the owning module's `jdocSpockTest`. For the engine integration, place this suite in `src/test/java/example/spock/JdocSpockTestSuite.java` alongside the dependencies and `useJUnitPlatform()` configuration in the [engine reference](junit-engines.md#spock-engine):

```java
package example.spock;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("jdoc-spock")
@SelectDirectories("src/main/java")
public class JdocSpockTestSuite {}
```

The suite selects the production source directory. Adding an engine dependency alone does not tell it where to read the Javadocs. Confirm the reports contain the features, including all three `Divider` table rows and the exception case.

## Library source references

For more complex cases, read the **whole declaring class**, including its constructor and imports:

- [SpecClassMapper](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-spock/src/main/java/org/bool/jdoc/spock/SpecClassMapper.java): `$target`, constructor collaborators, and stubbed return values.
- [ResourceContainer](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-spock-commons/src/main/java/org/bool/jdoc/spock/ResourceContainer.java): class-level tests of resource cleanup with explicit and generated mocks.
- [GroovyCompiler](https://github.com/boolivar/jdoc-test/blob/2e09552b353e9f559cf13f1f1d05f62599b7d219/jdoc-spock/src/main/java/org/bool/jdoc/spock/GroovyCompiler.java): a specification field annotated with Spock's `@TempDir` and an explicitly constructed subject.
