# jdoc-test agent skills

- [jdoc-spock](skills/jdoc-spock/SKILL.md) helps write Spock specs in Javadocs.
- [jdoc-cucumber](skills/jdoc-cucumber/SKILL.md) helps write Gherkin features in Javadocs.

Both skills include setup instructions for their Gradle plugin and JUnit Platform engine.

## Installation

Run this in your project directory and select the skills you want.

```sh
npx skills add boolivar/jdoc-test
```

To select a skill directly, add `--skill jdoc-spock` or `--skill jdoc-cucumber`.

For a manual install, copy the whole skill folder from [skills/](skills/) into your agent's skills directory.

## Usage

Ask your agent to use the skill by name and specify the Java class or method to document.
