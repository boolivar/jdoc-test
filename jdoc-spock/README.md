# jdoc-spock

> Part of [jdoc-test](../README.md). Runs [spockframework](https://spockframework.org/) test specifications written in javadocs.

`jdoc-spock` contains a junit platform engine to run tests. It considers text in javadoc or block comment between `<code lang="spock">` `</code>` tags as spock specification code.
Additional non-mandatory `<pre>` tag keeps code formatting for javadoc presentation:

<!-- x-release-please-start-version -->
```xml
<dependency>
    <groupId>io.github.boolivar.jdoctest</groupId>
    <artifactId>jdoc-spock</artifactId>
    <version>0.13.0</version>
    <scope>test</scope>
</dependency>
```
<!-- x-release-please-end -->

---

1. Write `jdoc-spock` tests.

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
<!-- x-release-please-start-version -->
```gradle
repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly "io.github.boolivar.jdoctest:jdoc-spock:0.13.0"
}
```
<!-- x-release-please-end -->

> [!IMPORTANT]
> `jdoc-spock` versions before `0.9.0` available only on [jitpack](https://jitpack.io/#boolivar/jdoc-test).
>
> ```gradle
> repositories {
>     maven { url "https://jitpack.io" }
> }
> 
> dependencies {
>     testRuntimeOnly "io.github.boolivar.jdoctest:jdoc-spock:0.8.1"
> }
> ```

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
