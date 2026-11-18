# jdoc-test agent plugin

Reusable guidance for writing executable Spock and Gherkin tests in Java Javadocs, configuring the Gradle plugins, and using the JUnit engines.

The single `jdoc-test` plugin contains two portable skills:

- [jdoc-spock](skills/jdoc-spock/SKILL.md): Spock Javadoc tests, constructor mocks, `$target`, and execution setup.
- [jdoc-cucumber](skills/jdoc-cucumber/SKILL.md): Gherkin Javadoc tests, matching Cucumber glue, and execution setup.

Each skill contains its own references and can be installed independently. Maintain both skills here. The [Claude manifest](.claude-plugin/plugin.json) is the source of truth for the plugin and its listing. Release-please updates its `version` field to match the project release.

## Local use

From the repository root, load the plugin in Claude Code:

```sh
claude --plugin-dir ./agent-plugin
```

Invoke the skills using the syntax for your agent and installation:

| Installation | Spock | Cucumber |
| --- | --- | --- |
| Claude Code plugin | `/jdoc-test:jdoc-spock` | `/jdoc-test:jdoc-cucumber` |
| Claude Code standalone skills | `/jdoc-spock` | `/jdoc-cucumber` |
| Codex | `$jdoc-spock` | `$jdoc-cucumber` |

For example, ask `$jdoc-spock Add Javadoc tests to OrderService` or `/jdoc-test:jdoc-cucumber Add scenario outlines for discount calculation`. Agents can also select the appropriate skill automatically from the request.

To install across projects without a plugin, copy the entire `skills/jdoc-spock` and/or `skills/jdoc-cucumber` directory into your agent's user skill directory: `~/.agents/skills/` for Codex or `~/.claude/skills/` for Claude Code. These are copies; refresh them when updating the repository source. When replacing the previous standalone `jdoc-test` skill, remove its old installed directory to avoid retaining outdated guidance.

## Validate and package

Each CI run uploads this directory in an independent `agent-plugin` job as the `jdoc-test-agent-plugin-<version>` workflow artifact. The version comes from the Claude manifest. `actions/upload-artifact` creates the ZIP directly and includes the hidden Claude manifest directory. Packaging runs alongside the Java build, including for `docs` and `chore` commits that skip the Java build. The workflow's branch filters still apply. Download the artifact from the workflow run to obtain the plugin ZIP.

With Claude Code installed, run from the repository root:

```sh
claude plugin validate ./agent-plugin
```

Test the installed skills in a fresh session, including their relative references. To package manually, include `.claude-plugin/plugin.json`, `skills/`, `assets/`, and `LICENSE`. The ZIP may contain these entries at its root or inside a single top-level directory.

## OpenAI submission

When skills-only submissions are available to the publisher, upload the CI artifact through OpenAI's **Skills only** flow. The portal converts the Claude manifest to `.codex-plugin/plugin.json` and supplies listing defaults. Review the generated manifest and test both imported skills. See [OpenAI's Claude plugin import guide](https://developers.openai.com/plugins/guides/submit-claude-plugin).

Use the Claude manifest's `description` for the subtitle. Its `metadata.openai` object records the intended category and long description for review. Check those values, the website, support, privacy and terms URLs, and the bundled icon in the converted listing. Fill any missing fields from the Claude manifest. Publish `docs/agent-plugin-privacy.md` with the project's GitHub Pages site and confirm that `privacyPolicyUrl` opens while signed out.

Keep review adjustments in this repository and regenerate the candidate ZIP from that source. Coordinate directory review with the project release so the final tag and submitted package contain the same skill source. Retain the submitted ZIP with that release. For later updates, let release-please synchronize the manifest version with the project and submit a complete package again.

Claude directory distribution has its own submission process; see [Claude plugin documentation](https://code.claude.com/docs/en/plugins). A marketplace catalog can be added separately if installation from a repository marketplace is desired.
