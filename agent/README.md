# jdoc-test Agent Skills

Use these skills to write executable tests in Java Javadocs and configure jdoc-test with Gradle or JUnit Platform.

- [jdoc-spock](skills/jdoc-spock/SKILL.md) covers Spock feature methods, constructor mocks, and `$target`.
- [jdoc-cucumber](skills/jdoc-cucumber/SKILL.md) covers Gherkin scenarios and matching Cucumber step definitions.

Each skill has its own `SKILL.md` and supporting files and can be installed independently. [Agent Skills specification](https://agentskills.io/specification)

## Install

Run this from your Java project and select the skills you want.

```sh
npx skills add boolivar/jdoc-test
```

For a manual install, copy the entire `skills/jdoc-spock` or `skills/jdoc-cucumber` directory into a skills location supported by your agent. Keep the `references/` directory with its `SKILL.md`.

## Use

- Ask your agent to use `jdoc-spock` to add executable Spock examples to the Javadocs for `OrderService`.
- Ask your agent to use `jdoc-cucumber` to add Gherkin scenarios and matching step definitions for discount calculations.
