# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**jdoc-test** is a Java framework that enables writing BDD tests directly inside Javadoc comments. Tests are written in Spock (Groovy) or Gherkin (Cucumber feature) format within `<code lang="spock">` or `<code lang="gherkin">` Javadoc tags, then automatically extracted, compiled, and executed by JUnit Platform engines. The project dogfoods itself — production source javadocs contain test specs that are run by the project's own engines.

**Group/Artifact:** `io.github.boolivar.jdoctest` | **Java:** source compat 8 (JDK 21 toolchain) | **Build:** Gradle 8.14.5 (Kotlin DSL + Groovy) | **Version:** 0.12.0

## Module Structure

```
jdoc-core                      — Core parsing (Java file parser, RegexParser, SpecSource model)
├── jdoc-spock-commons         — Shared Spock components (spec generator, constructor introspector)
├── jdoc-junit-engine-commons  — Shared JUnit engine components (config params, spec reader)
│   ├── jdoc-spock             — JUnit Platform engine for Spock specs from javadocs
│   └── jdoc-cucumber          — JUnit Platform engine for Gherkin features from javadocs
├── jdoc-gradle-plugin-commons — Shared Gradle plugin infrastructure (JdocAction, JdocTask)
│   ├── jdoc-spock-gradle-plugin     — Gradle plugin automating Spock spec generation/testing
│   └── jdoc-cucumber-gradle-plugin  — Gradle plugin automating Cucumber feature generation/testing
└── jdoc-platform              — BOM constraining Cucumber, JUnit, and library versions
```

All subprojects are auto-discovered via `settings.gradle` (recursive scan excluding `buildSrc`). Dependencies follow a layered model: `jdoc-core` → `jdoc-*commons` → engines/plugins.

## Build Conventions

`buildSrc` holds reusable convention plugins applied via `alias(libs...)` or `id(...)` in module build files:

- **java-conventions** — Base: JDK 21 toolchain, JaCoCo (50% min), Checkstyle, PMD, SpotBugs, Lombok, JSpecify, test deps (JUnit, AssertJ, Mockito)
- **java-library-conventions** — Adds publishing + JSpecify API
- **java-library-jdoc-spock-conventions** — Adds `-parameters` compiler arg + configures self-testing via jdoc-spock engine
- **gradle-plugin-conventions** — Shadow plugin with package relocation for fat JARs
- **jspecify-conventions** (Kotlin DSL) — Error Prone + NullAway; auto-generates `package-info.java` with `@NullMarked`

## Commands

**Full build & all checks:**
```bash
./gradlew check
```
`check` runs: unit tests, Checkstyle, PMD, SpotBugs, JaCoCo verification, Error Prone, and NullAway.

**Build a JAR (includes checks):** `./gradlew jar`

**Run a single test:**
```bash
./gradlew :<module>:test --tests "<fully.qualified.ClassName>.<methodName>"
# e.g. ./gradlew :jdoc-core:test --tests "org.bool.jdoc.core.RegexParserTest.testParse"
```

**Lint/analyze individual modules:**
```bash
./gradlew :<module>:checkstyleMain :<module>:pmdMain :<module>:spotbugsMain
# Test variant: replace Main → Test
```

**Publish to local Maven (skip checks/signing — used by JitPack):**
```bash
./gradlew -xcheck -xsignMavenPublication publishToMavenLocal
```

## Test Architecture

**Unit tests** use JUnit 5 (Jupiter) with AssertJ and Mockito. Tests are package-private, use `@ExtendWith(MockitoExtension.class)`, and follow `@ParameterizedTest` with `@MethodSource`/`@ValueSource`. Some use `@TestInstance(Lifecycle.PER_CLASS)` for instance `@BeforeAll`.

**Self-testing (dogfooding):** Modules with jdoc-spock/cucumber deps include a `JdocSpockTestSuite` (or `JdocCucumberTestSuite`) test class using `@Suite` + `@IncludeEngines("jdoc-spock")` + `@SelectDirectories("src/main/java")` to discover and run specs embedded in the module's own main-source javadocs.

**Gradle plugin tests** use `GradleRunner` and `ProjectBuilder` to test plugin application, task registration, and full Gradle builds.

## Code Style

- 4-space indentation; line length 140 (warning)
- Import order (Checkstyle `ImportOrder`): `org.bool` → third-party `*` → `java`/`javax`; static imports sorted alphabetically
- Lombok: `@AllArgsConstructor`, `@Data`, `@Builder`, `@NoArgsConstructor`, `@SneakyThrows`, `@NonNull`, `@Slf4j`
- `@SuppressWarnings("PMD.<RuleName>")` to suppress specific PMD rules
- `package-info.java` auto-generated with `@NullMarked` (via jspecify-conventions); do not manually create these
- Error Prone and NullAway are enforced as errors on main source sets; disabled on test source sets
