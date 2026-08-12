# jdoc-test

[![CI](https://github.com/boolivar/jdoc-test/workflows/CI/badge.svg)](https://github.com/boolivar/jdoc-test/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/boolivar/jdoc-test/graph/badge.svg?token=PCV1VTNFYT)](https://codecov.io/gh/boolivar/jdoc-test)
[![codeclimate](https://api.codeclimate.com/v1/badges/5abdb712f0e232643f83/maintainability)](https://codeclimate.com/github/boolivar/jdoc-test/maintainability)
[![jitpack](https://jitpack.io/v/boolivar/jdoc-test.svg)](https://jitpack.io/#boolivar/jdoc-test)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.boolivar.jdoctest/jdoc-core)](https://central.sonatype.com/namespace/io.github.boolivar.jdoctest)

[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)
[![License](https://img.shields.io/github/license/boolivar/jdoc-test)](LICENSE)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=boolivar_jdoc-test&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=boolivar_jdoc-test)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=boolivar_jdoc-test&metric=bugs)](https://sonarcloud.io/summary/new_code?id=boolivar_jdoc-test)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=boolivar_jdoc-test&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=boolivar_jdoc-test)

Write BDD tests in javadocs!

Documentation (AI generated): https://boolivar.github.io/jdoc-test/

```java
public class MathUtils {
    /**
     * Calculate square of x.
     * 
     * <pre><code lang="spock">
     * def "returns square"() {
     *   expect:
     *     MathUtils.sqr(2) == 4
     * }
     * </code></pre>
     * 
     * <pre><code lang="gherkin">
     * Feature: square calculation
     *   Scenario Outline: integers
     *     When input value equals <x>
     *     Then result should be <sqr>
     *     Examples:
     *       | x | sqr |
     *       | 0 | 0   |
     *       | 1 | 1   |
     *       | 2 | 4   |
     *       |-1 | 1   |
     *       | 10| 100 |
     *       | 11| 121 |
     * </code></pre>
     */
    public static int sqr(int x) {
        return x * x;
    }
}
```

## What?

**jdoc-test** is a framework for javadoc sourced java tests.

Javadoc writing is cumbersome. Documentation quickly becomes outdated. There is no guarantee that code does what documentation says.  
Developers often prefer to write tests instead of documentation. Tests never lie.

So why not just write tests in documentation? [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) frameworks
use test specifications written in (more or less) human language. Such documentation goes in sync with actual code and shows code
usage example. Java code, tests and documentation become tightly coupled by putting BDD specification in javadoc.

## WHAT?

| Component | Type | Description |
| --------- | ---- | ----------- |
| [jdoc-spock](jdoc-spock/README.md) | junit engine library | Runs [spockframework](https://spockframework.org/) test specifications written in javadocs. |
| [jdoc-spock-gradle-plugin](jdoc-spock-gradle-plugin/README.md) | gradle plugin | [Automates](https://plugins.gradle.org/plugin/io.github.boolivar.jdoctest.jdoc-spock) spockframework specs generation and testing. |
| [jdoc-cucumber](jdoc-cucumber/README.md) | junit engine library | Runs [gherkin](https://cucumber.io/docs/gherkin/reference/) features written in javadocs. |
| [jdoc-cucumber-gradle-plugin](jdoc-cucumber-gradle-plugin/README.md) | gradle plugin | [Automates](https://plugins.gradle.org/plugin/org.bool.jdoctest.jdoc-cucumber) cucumber feature generation and testing. |

:warning: **Library tests itself using itself executing own `jdoc-spock` tests written in javadocs.**

## WHAT???

Yes, see `jdoc-spock` and `jdoc-cucumber` [test](https://github.com/boolivar/jdoc-test/blob/master/jdoc-spock-commons/src/main/java/org/bool/jdoc/spock/ResourceContainer.java) [examples](https://github.com/boolivar/jdoc-test/blob/master/jdoc-cucumber/src/main/java/org/bool/jdoc/cucumber/ConfigParams.java) in [source](https://github.com/boolivar/jdoc-test/blob/master/jdoc-core/src/main/java/org/bool/jdoc/core/JavaFileParser.java) [code](https://github.com/boolivar/jdoc-test/blob/master/jdoc-spock-commons/src/main/java/org/bool/jdoc/spock/ClassIntrospector.java).

## HOW?

See the component READMEs for detailed usage:

- **[jdoc-spock](jdoc-spock/README.md)** — write and run Spock specs from javadocs using JUnit engine
- **[jdoc-cucumber](jdoc-cucumber/README.md)** — write and run Gherkin features from javadocs using JUnit engine
- **[jdoc-spock-gradle-plugin](jdoc-spock-gradle-plugin/README.md)** — automate Spock spec generation and testing via Gradle
- **[jdoc-cucumber-gradle-plugin](jdoc-cucumber-gradle-plugin/README.md)** — automate Cucumber feature generation and testing via Gradle