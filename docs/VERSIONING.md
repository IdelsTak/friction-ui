# Versioning

Canonical versioning policy lives in `friction-core`:

- [friction-core/docs/VERSIONING.md](https://github.com/idelstak/friction-core/blob/master/docs/VERSIONING.md)

This repo follows that policy for release labeling and pre-release staging.

## Package Version Pins

`friction-ui` pins upstream package versions in `pom.xml`:

- `friction.adapters.version`

Update and adoption checklist is documented in `docs/PACKAGE_CONSUMPTION.md`.

## Release Version Bump

`friction-ui` release flow mirrors sibling repos:

- resolve merged PR labels
- compute next semantic version
- update `pom.xml`
- commit bump and create tag

## Changelog Format

Release notes are generated from current PR only:

- `# Changelog`
- `#<PR_NUMBER> <PR_TITLE>`
- cleaned PR body

No historical merged-PR aggregation is included.

## Release Packaging

Packaging model is documented in `docs/RELEASE_PACKAGING.md`.
