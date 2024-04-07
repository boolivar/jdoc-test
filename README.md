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

**jdoc-spock** library runs [spockframework](https://spockframework.org/) test specifications from javadocs.

:warning: **Library tests itself using itself executing own `jdoc-spock` tests written in javadocs.**

### WHAT???

Yes, see `jdoc-spock` test examples in `jdoc-spock` source code.

### How?

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
 * </code><pre>
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
    testRuntimeOnly "com.github.boolivar:jdoc-test:0.2.0"
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

4. Configure paths to java sources using test suite `@SelectDirectories` or `@SelectFile`:

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

5. Run tests using `jdoc-spock` junit engine.

`gradle` example:
```sh
gradle test
```
