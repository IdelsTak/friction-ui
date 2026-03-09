# Package Consumption (core + adapters)

This document defines how `friction-ui` consumes published packages from
GitHub Packages.

## Version Pins

`pom.xml` is the single pin point:

- `friction.adapters.version`

Rules:

- Use published versions only.
- Do not use local/manual jar wiring in CI.
- Update adapter pin intentionally during release adoption.

## Maven Repositories

`pom.xml` declares both package repositories:

- `github-core` -> `https://maven.pkg.github.com/idelstak/friction-core`
- `github-adapters` -> `https://maven.pkg.github.com/idelstak/friction-adapters`

## CI Authentication

`ci.yml` uses `secrets.PACKAGES_TOKEN` and writes `~/.m2/settings.xml`
with both server ids:

- `github-core`
- `github-adapters`

This is required for deterministic dependency resolution in CI.

## Local Authentication

Create `~/.m2/settings.xml` with matching server ids and credentials:

```xml
<settings>
  <servers>
    <server>
      <id>github-core</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PACKAGES_TOKEN</password>
    </server>
    <server>
      <id>github-adapters</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PACKAGES_TOKEN</password>
    </server>
  </servers>
</settings>
```

Use a token with `read:packages` scope.

## Upgrade Flow

1. Confirm new package versions are published in GitHub Packages.
2. Update `friction.adapters.version` in `pom.xml`.
3. Run `mvn -B -ntp verify` locally.
4. Open PR with one versioning label (`version:major|minor|patch`).
5. Confirm CI resolves both packages without local jar fallback.

## Dependency Update Checklist

- [ ] `friction.adapters.version` is updated intentionally.
- [ ] Versions exist in GitHub Packages.
- [ ] Local build succeeds with settings-based auth.
- [ ] CI build succeeds with `PACKAGES_TOKEN`.

## Transitive Dependency Note

`friction-ui` depends directly on `friction-adapters` only.
`friction-core` is resolved transitively via `friction-adapters`.

Both repositories (`github-adapters` and `github-core`) remain configured so
Maven can resolve both direct and transitive artifacts.
