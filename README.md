# minecraft-build

Shared Gradle build configuration for Ejafi Games NeoForge mods. The `com.ejafi.neoforge-mod` convention plugin applies ModDevGradle, Error Prone with NullAway, and Spotless, and sets up the source sets, runs, and test dependencies every mod uses.

## Using the plugin

The plugin is published to GitHub Packages, which requires a token even to read. Add a personal access token with the `read:packages` scope to your user `~/.gradle/gradle.properties`:

```properties
GitHubPackagesUsername=<your GitHub username>
GitHubPackagesPassword=<the token>
```

In CI, set `ORG_GRADLE_PROJECT_GitHubPackagesUsername` to `${{ github.actor }}` and `ORG_GRADLE_PROJECT_GitHubPackagesPassword` to `${{ secrets.GITHUB_TOKEN }}`.

The mod's `settings.gradle` adds the repository. The `exclusiveContent` filter makes `com.ejafi` groups resolve only from GitHub Packages, so a plugin published under the same ID elsewhere can never replace this one.

```groovy
pluginManagement {
    repositories {
        exclusiveContent {
            forRepository {
                maven {
                    name = 'GitHubPackages'
                    url = 'https://maven.pkg.github.com/EjafiSoftware/minecraft-build'
                    credentials(PasswordCredentials)
                }
            }
            filter {
                includeGroupByRegex 'com\\.ejafi\\..*'
            }
        }
        gradlePluginPortal()
    }
}
```

The mod's `build.gradle` applies the plugin and declares only the dependencies that mod alone uses:

```groovy
plugins {
    id 'com.ejafi.neoforge-mod' version '1.1.0'
}
```

The mod's `gradle/gradle-daemon-jvm.properties` must pin the daemon to JDK 21, because the plugin's runs and compile tasks expect a full JDK.

### Required properties

The plugin reads these from the mod's `gradle.properties`, and the build fails naming any that is missing.

| Property | Use |
|---|---|
| `minecraft_version`, `minecraft_version_range` | Expanded into `neoforge.mods.toml` |
| `neo_version` | NeoForge version for the build and for `net.neoforged:testframework` |
| `neo_version_range`, `loader_version_range` | Expanded into `neoforge.mods.toml` |
| `neoForge.parchment.minecraftVersion`, `neoForge.parchment.mappingsVersion` | Parchment mappings, read by ModDevGradle |
| `mod_id` | Mod ID, archive name, GameTest namespace, and data run `--mod` |
| `mod_name`, `mod_license`, `mod_version` | Expanded into `neoforge.mods.toml`; `mod_version` is also the project version |
| `mod_group_id` | Project group |

### What a mod gets

- **Source sets.** `main`, `test`, `testFixtures`, `gameTest`, and `datagen`. `gameTest` sees `main` and `testFixtures`, and `datagen` sees `main`. All five are bound to the mod, so the dev runs and the unit-test runtime load them as one mod, and the jar contains only `main`.
- **Runs.** `client`, `server`, and `gameTestServer` run the `gameTest` source set with the mod's GameTest namespace enabled. `data` runs the `datagen` source set and writes to `src/generated/resources/`. Each run uses `runs/<name>` as its game directory.
- **Resources.** `src/generated/resources/` is a resource directory with its `.cache` excluded. `src/main/templates/META-INF/neoforge.mods.toml` is expanded with the properties above.
- **Compilation.** Java 21, UTF-8, and NullAway at error severity in `OnlyNullMarked` mode, so a package is checked once its `package-info.java` carries `@NullMarked`.
- **Formatting.** Spotless with palantir-java-format over `src/*/java/**`. `./gradlew spotlessApply` formats every source set.
- **Dependencies.** JSpecify as `compileOnly` everywhere. JUnit Jupiter, AssertJ, and `testframework` for unit tests. Datafaker for test fixtures, GameTests, and the dev runs.

## CI and releases

A mod's `.github/workflows/build.yml` calls the two reusable workflows in this repo, pinned to the same version as the plugin:

```yaml
name: Build

on:
  push:
    branches: [main]
    tags: ['[0-9]+.[0-9]+.[0-9]+']
  pull_request:

jobs:
  gate:
    uses: EjafiSoftware/minecraft-build/.github/workflows/mod-gate.yml@v1.1.0
    permissions:
      contents: read
      packages: read

  release:
    if: github.ref_type == 'tag'
    needs: gate
    uses: EjafiSoftware/minecraft-build/.github/workflows/mod-release.yml@v1.1.0
    permissions:
      contents: write
```

- **`mod-gate.yml`** runs `runData` and fails if it changes any committed file, then runs `build` and `runGameTestServer`. It uploads the mod jar, without the test-fixtures jar, as the `mod-jar` artifact.
- **`mod-release.yml`** runs for a tag that matches `mod_version` exactly, such as `2.1.0`, on a commit in `main`. It creates a GitHub release titled with the version, attaches the jar, and takes the notes from the `## 2.1.0` or `## [2.1.0]` section of `CHANGELOG.md`. A tag that doesn't match `mod_version`, a commit outside `main`, or a missing changelog section fails the job before anything is published.

## Working on the plugin locally

A mod builds against a working copy of this repo when its `settings.gradle` includes it inside `pluginManagement`. The included build replaces the published plugin even when the `plugins` block names a version.

```groovy
pluginManagement {
    includeBuild '../minecraft-build'
}
```

## Releasing

Set `version` in `gradle.properties`, then push a tag named `v<version>`. CI builds the plugin and runs the smoke mod through `mod-gate.yml`, then publishes the plugin and its marker to GitHub Packages. The same tag is the ref that mods pin the reusable workflows to.
