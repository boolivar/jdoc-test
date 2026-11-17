# jdoc-test agent plugin

Reusable guidance for writing executable Spock and Gherkin tests in Java Javadocs, configuring the Gradle plugins, and using the JUnit engines.

The portable skill source is [skills/jdoc-test/SKILL.md](skills/jdoc-test/SKILL.md). Maintain it and its references here. The plugin uses a single [.claude-plugin/plugin.json](.claude-plugin/plugin.json) manifest and shares the jdoc-test project release version. Release-please updates its `version` field through the JSON updater configured in the repository's `release-please-config.json`.

## Local use

From the repository root, load the plugin in Claude Code:

```sh
claude --plugin-dir ./agent-plugin
```

The skill is available as `/jdoc-test:jdoc-test`. To install the skill across projects without a plugin, copy the entire `skills/jdoc-test` directory into your agent's user skill directory: `~/.agents/skills/` for Codex or `~/.claude/skills/` for Claude Code. These are copies; refresh them when updating the repository source.

## Validate and package

Each CI run uploads this directory in an independent `agent-plugin` job as the `jdoc-test-agent-plugin-<version>` workflow artifact. The version comes from the plugin manifest. `actions/upload-artifact` creates the ZIP directly and includes the hidden manifest directory. Packaging runs alongside the Java build, including for `docs` and `chore` commits that skip the Java build. The workflow's branch filters still apply. Download the artifact from the workflow run to obtain the plugin ZIP for submission; there is no nested ZIP to extract.

With Claude Code installed, run from the repository root:

```sh
claude plugin validate ./agent-plugin
```

Test the installed skill in a fresh session, including its relative references. Archive this directory with `.claude-plugin/plugin.json`, `skills/`, and `LICENSE` included. The ZIP may contain these entries at its root or inside a single top-level directory. Make sure the archive tool includes the hidden `.claude-plugin` directory.

## OpenAI submission

OpenAI supports importing a skills-only Claude plugin archive. Upload the ZIP through the plugin submission portal's **Skills only** flow, review the generated `.codex-plugin/plugin.json`, test the imported skill, and complete the listing and review steps. Conversion happens in the portal and does not change this repository. See [OpenAI's import guide](https://developers.openai.com/plugins/guides/submit-claude-plugin).

Keep review adjustments in this repository and regenerate the candidate ZIP from that source. Coordinate directory review with the project release so the final tag and submitted package contain the same skill source. Retain the submitted ZIP with that release. For later updates, let release-please synchronize the manifest version with the project and submit a complete package again.

Claude directory distribution has its own submission process; see [Claude plugin documentation](https://code.claude.com/docs/en/plugins). A marketplace catalog can be added separately if installation from a repository marketplace is desired.
