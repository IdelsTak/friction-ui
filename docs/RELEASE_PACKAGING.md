# Release Packaging

`friction-ui` release output is non-shaded and consists of:

- `friction-ui-<version>.jar`
- runtime dependencies in `libs/`

## Packaging Model

`pom.xml` uses:

- `maven-jar-plugin` to build a non-shaded jar
- manifest entries:
  - `Main-Class: com.github.idelstak.friction.ui.FrictionUi`
  - classpath enabled with `libs/` prefix
- `maven-dependency-plugin` to copy runtime dependencies to `target/libs/`

## Runtime Execution

From packaged output directory:

```bash
java -cp "friction-ui-<version>.jar:libs/*" com.github.idelstak.friction.ui.FrictionUi
```

## Release Workflow Model

- `release.yml`
  - push to `master`
  - resolves semver bump from PR labels
  - updates `pom.xml` version
  - commits bump, tags release, creates GitHub release notes
  - changelog format:
    - `# Changelog`
    - `#<PR_NUMBER> <PR_TITLE>`
    - cleaned PR body

- `publish-ui.yml`
  - runs after successful `Release`
  - checks out latest semver tag
  - validates tag version matches `pom.xml`
  - runs `mvn package`
  - builds `jpackage` app-image artifacts on:
    - Linux
    - Windows
    - macOS
  - uploads release assets:
    - `friction-ui-<version>-linux-app-image.tar.gz`
    - `friction-ui-<version>-windows-app-image.zip`
    - `friction-ui-<version>-macos-app-image.tar.gz`
    - `friction-ui-<version>-jar-libs.tar.gz` (non-shaded jar + `libs/`, Linux job)

## Version Bump Notes

Version bump behavior follows the same PR-label semver pattern used in sibling repos.
