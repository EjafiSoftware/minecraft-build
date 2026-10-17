# AGENTS.md

Guidance for coding agents working in this repository. [README.md](README.md) describes what the plugin configures and how a mod consumes it.

## Layout

| Path | Contents |
|---|---|
| `convention-plugin/src/main/groovy/com.ejafi.neoforge-mod.gradle` | The convention plugin, a Groovy precompiled script plugin |
| `convention-plugin/src/main/resources/com/ejafi/gradle/versions.properties` | Template that `processResources` fills with library versions, which the plugin reads at apply time |
| `gradle/libs.versions.toml` | Every version the plugin uses: the plugins it applies, as `implementation` dependencies of `convention-plugin`, and the libraries it adds to a mod |
| `smoke-mod/` | A separate Gradle build that applies the plugin through `includeBuild '..'`. Its block, unit test, GameTest, test fixture, and language provider exercise each source set the plugin sets up. |

A version change is made only in `gradle/libs.versions.toml`. A new library version also needs its key in `convention-plugin/build.gradle` and in `versions.properties`.

## Commands

| Command | What it does |
|---|---|
| `./gradlew build` | Builds and validates the convention plugin |
| `./gradlew -p smoke-mod runData` | Regenerates the smoke mod's `src/generated/resources/` |
| `./gradlew -p smoke-mod build` | Spotless check, compile under NullAway, and the unit test |
| `./gradlew -p smoke-mod runGameTestServer` | Runs the smoke mod's GameTests and exits |
| `./gradlew -p smoke-mod spotlessApply` | Formats the smoke mod's sources |

**The gate** is `./gradlew build`, then the smoke mod's `runData` with `git status --porcelain` printing nothing, then its `build`, then its `runGameTestServer`. CI (`.github/workflows/build.yml`) runs the same steps, and on a `v*` tag also publishes the plugin.

## Coding standards

The smoke mod follows the frie-skills skills: `brandon-standards` with its `references/java.md`, `neoforge-modding`, and `engineering-patterns`.
