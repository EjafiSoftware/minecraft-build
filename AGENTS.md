# AGENTS.md

This file guides coding agents working in this repository. [README.md](README.md) describes what the plugin configures and how a mod consumes it.

## Layout

| Path | Contents |
|---|---|
| `convention-plugin/src/main/groovy/com.ejafi.neoforge-mod.gradle` | The convention plugin, a Groovy precompiled script plugin |
| `convention-plugin/src/main/resources/com/ejafi/gradle/versions.properties` | Template that `processResources` fills with library versions, which the plugin reads at apply time |
| `convention-plugin/src/main/resources/com/ejafi/gradle/pmd-ruleset.xml` | The PMD ruleset every mod runs |
| `gradle/libs.versions.toml` | Every version the plugin uses, both for the plugins it applies, which are `implementation` dependencies of `convention-plugin`, and for the libraries it adds to a mod |
| `smoke-mod/` | A separate Gradle build that applies the plugin through `includeBuild '..'`. Its block, unit test, GameTest, test fixture, and language provider exercise each source set the plugin sets up. |
| `.github/workflows/mod-gate.yml`, `mod-codeql.yml`, `mod-release.yml` | Reusable workflows that mods call for their gate with dependency review, their CodeQL analysis, and their tagged releases. `mod-gate.yml` and `mod-codeql.yml` take a `project-dir` input, which this repo's CI sets to `smoke-mod`. |
| `.github/workflows/build.yml` | This repo's CI, which builds the plugin, runs the smoke mod through `mod-gate.yml` and `mod-codeql.yml`, and publishes on a `v*` tag |
| `.github/dependabot.yml` | Weekly grouped updates for the `gradle` ecosystem in this build and the smoke mod, and for `github-actions`. `gradle/actions` stays on v5, because v6 puts its caching under Gradle's proprietary Terms of Use. |

A version change is made only in `gradle/libs.versions.toml`. A library the plugin adds to a mod is a `[libraries]` entry, so Dependabot can update it, and also needs its key in `convention-plugin/build.gradle` and in `versions.properties`.

## Commands

| Command | What it does |
|---|---|
| `./gradlew build` | Builds and validates the convention plugin |
| `./gradlew -p smoke-mod runData` | Regenerates the smoke mod's `src/generated/resources/` |
| `./gradlew -p smoke-mod build` | Spotless check, compile under NullAway, PMD, CPD, and the unit test |
| `./gradlew -p smoke-mod runGameTestServer` | Runs the smoke mod's GameTests and exits |
| `./gradlew -p smoke-mod spotlessApply` | Formats the smoke mod's sources |

**The gate** is `./gradlew build`, then the smoke mod's `runData` with `git status --porcelain` printing nothing, then its `build`, then its `runGameTestServer`. CI runs the same steps, with the smoke mod's steps going through `mod-gate.yml`, and on a `v*` tag also publishes the plugin. A change to a reusable workflow reaches a mod only when the mod moves its pinned ref to a new tag.

## Coding standards

The smoke mod follows the frie-skills skills `brandon-standards` with its `references/java.md`, `neoforge-modding`, and `engineering-patterns`.
