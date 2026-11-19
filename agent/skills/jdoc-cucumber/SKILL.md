---
name: jdoc-cucumber
description: Add or maintain executable Gherkin tests inside Java Javadocs using jdoc-test, including Cucumber step definitions and Gradle plugin or JUnit engine setup. Use for projects adopting jdoc-cucumber or editing existing Gherkin Javadoc tests.
license: MIT
---

# jdoc-cucumber

jdoc-test extracts `<code lang="gherkin">` blocks from Java Javadocs or block comments. Prefer Javadocs when examples should appear in API documentation; `<pre>` around `<code>` preserves display formatting. These are executable tests, so keep each example tied to the public behavior it describes.

## Work in a target project

1. Inspect the target project's build files and existing test conventions. Search across all modules and source sets, including `src/main/java`, for language-tagged code blocks, `jdocCucumber`, and `jdoc-cucumber`, and locate existing step definitions. Include custom language tags configured in the build. Production Java files may contain the tests; read representative blocks and their discovery configuration before adding new ones.
2. Use the existing jdoc-cucumber integration if one is present. For new Gradle projects, prefer the Gradle plugin for generated features and a dedicated test task. Use the JUnit engine when the project already selects source paths through JUnit Platform suites or needs an engine without the plugin. See [Gradle plugin setup](references/gradle-plugins.md) or [JUnit engine setup](references/junit-engines.md) as relevant.
3. Put a complete Gherkin `Feature` in each language-tagged block. Follow nearby names, indentation, and step definitions. Blocks become separate features and need matching Cucumber glue; reuse existing steps when their behavior matches.
4. Preserve the build's existing version catalog, repositories, source sets, and plugin conventions. Set up each Java module that owns Gherkin Javadoc tests; a root-only plugin application does not configure subprojects. Wire `jdocCucumberTest` into `check` only if the project wants it in the normal verification lifecycle.

## Author tests in their Java context

Keep the test beside the behavior it documents, in the class or method Javadoc. When presenting examples, include the declaring class, relevant constructors, tested implementation, and any collaborator interfaces needed to understand them. For edits to an existing class, preserve that context rather than replacing it with a detached test snippet.

Read [Cucumber examples](references/cucumber-examples.md) for complete declaring classes, matching glue, scenario outlines, and suite configuration. Gherkin does not receive `$target` or generated mocks. Step definitions instantiate or obtain the subject and implement every step. Keep glue in the test source set and align its package with the selected integration's glue configuration. Source selection locates the Java comments; glue configuration locates the step implementations. Both must match the project.

## Verification

Use the target project's normal build launcher and applicable execution instructions. The Gradle plugin runs through `jdocCucumberTest`; JUnit engine integrations run through the configured test task or test runner. Select the affected module and confirm that its scenarios were discovered with no undefined or ambiguous steps, since a successful task with no tests does not validate an example. Report the command and outcome, or identify any verification that was not performed.

## Source of truth

Use the [upstream repository](https://github.com/boolivar/jdoc-test), its component READMEs, and implementation matching the target project's release to resolve behavior. The reference snippets use `0.12.0` as an example, not a latest-version claim. Preserve an existing compatible version; when adopting jdoc-cucumber, select a published release compatible with the project's Java, Gradle, Cucumber, and JUnit versions. Dependency defaults in a development checkout may differ from a published artifact.
