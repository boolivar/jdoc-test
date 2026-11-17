---
name: jdoc-test
description: Add or maintain executable Spock or Gherkin tests inside Java Javadocs using jdoc-test, including Gradle plugin or JUnit engine setup. Use for Java projects adopting jdoc-test or editing existing jdoc-test examples.
---

# jdoc-test

jdoc-test extracts `<code lang="spock">` and `<code lang="gherkin">` blocks from Java Javadocs or block comments. Prefer Javadocs when examples should appear in API documentation; `<pre>` around `<code>` preserves display formatting. These are executable tests, so keep each example tied to the public behavior it describes.

## Work in a target project

1. Inspect the target project's build files and existing test conventions. Search across all modules and source sets, including `src/main/java`, for language-tagged code blocks, `jdocSpock`, `jdocCucumber`, `jdoc-spock`, and `jdoc-cucumber`. Include custom language tags configured in the build. Production Java files may contain the tests; read representative blocks and their discovery configuration before adding new ones.
2. Use the existing jdoc-test mode if one is present. For new Gradle projects, prefer the relevant Gradle plugin for generated test sources and a dedicated test task. Use the JUnit engine when the project already selects source paths through JUnit Platform suites or needs an engine without the plugin. See [Gradle plugin setup](references/gradle-plugins.md) or [JUnit engine setup](references/junit-engines.md) as relevant.
3. Put Spock specification members (feature methods, fields, or fixtures) or a complete Gherkin `Feature` in a language-tagged block. Follow nearby names, imports, indentation, fixture style, and step definitions. Spock blocks contribute to one generated specification for the Java file's primary type; do not wrap them in another specification class. Gherkin blocks become features and need matching Cucumber glue.
4. Preserve the build's existing version catalog, repositories, source sets, and plugin conventions. Set up each Java module that owns Javadoc tests; a root-only plugin application does not configure subprojects. Wire the dedicated plugin test task into `check` only if the project wants it in the normal verification lifecycle.

## Author tests in their Java context

Keep the test beside the behavior it documents, in the class or method Javadoc. When presenting examples, include the declaring class, relevant constructors, tested implementation, and any collaborator interfaces needed to understand them. For edits to an existing class, preserve that context rather than replacing it with a detached test snippet.

- **Spock:** read [Spock examples](references/spock-examples.md) for constructor mocks, `$target`, data tables, exceptions, and explicit construction. The generated specification inherits the Java file's package and imports. `$target` is an instance of the Java file's primary class, initialized through a suitable constructor; its collaborator mocks are named after constructor parameters retained by `-parameters`. Invoke instance behavior through `$target` when it is generated. `this` inside a feature refers to the specification, not the Java subject. Do not redeclare generated fields. See [constructor rules](references/gradle-plugins.md#spock) for absent targets and ambiguous constructors. Use fully qualified test-only types when needed; imports inside a block would land inside the generated class.
- **Cucumber:** read [Cucumber examples](references/cucumber-examples.md) for a complete declaring class, matching glue, scenario outlines, and suite configuration. Gherkin does not receive `$target` or generated mocks. Step definitions instantiate or obtain the subject and implement every step. Keep glue in the test source set and align its package with the selected integration's glue configuration.

## Verification

Use the target project's normal build launcher and applicable execution instructions. The Gradle plugins run through `jdocSpockTest` or `jdocCucumberTest`; JUnit engine integrations run through the configured test task or test runner. Select the affected module and confirm that its Javadoc tests were discovered, since a successful task with no tests does not validate an example. Report the command and outcome, or identify any verification that was not performed.

## Source of truth

Use the [upstream repository](https://github.com/boolivar/jdoc-test), its component READMEs, and implementation matching the target project's release to resolve behavior. The reference snippets use `0.12.0` as an example, not a latest-version claim. Preserve an existing compatible version; when adopting jdoc-test, select a published release compatible with the project's Java, Gradle, Groovy, and JUnit versions. Dependency defaults in a development checkout may differ from a published artifact.
