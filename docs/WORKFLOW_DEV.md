# Workflow Development and Local Preflight

Use this guide to validate GitHub Actions workflows locally before pushing.

## Required Tools

- `actionlint`
- `yamllint`
- `act`

## Installation

### Arch Linux (`pacman`)

```bash
sudo pacman -S --needed actionlint yamllint act
```

### actionlint

- Project: `https://github.com/rhysd/actionlint`
- Example (Go): `go install github.com/rhysd/actionlint/cmd/actionlint@latest`

### yamllint

- Example (pipx): `pipx install yamllint`
- Alternative: `pip install yamllint`

### act

- Project: `https://github.com/nektos/act`
- Install using package manager or official release binary

## Run Local Preflight

From repo root:

```bash
scripts/check-workflows.sh
```

What it does:

1. Verifies required tools are installed.
2. Runs `actionlint`.
3. Runs `yamllint .github/workflows`.
4. Runs `act` dry-run smoke checks for key workflows if present:
   - `ci.yml`
   - `release.yml`
   - `publish-ui.yml`

## `act` Limitations for Cross-Platform `jpackage`

`friction-ui` releases are expected to package cross-platform artifacts with `jpackage` on Linux, macOS, and Windows runners.

Local `act` caveats:

- `act` runs in local containerized context and may not match GitHub-hosted OS environments exactly.
- Cross-platform `jpackage` packaging cannot be fully validated in one local environment.
- Use `act` as workflow wiring smoke test, not final packaging parity proof.

Recommended practice:

- Validate workflow structure locally with `actionlint` + `yamllint` + `act` dry-run.
- Validate real packaging behavior with at least one GitHub-hosted run per target platform.

## Version Label and Required Check Alignment

Ensure workflow checks align with repo release policy:

- PRs should include exactly one impact label:
  - `version:major` or `version:minor` or `version:patch`
- Optional one pre-release label:
  - `version:alpha` or `version:beta` or `version:rc`
- Stable PR check names from `.github/workflows/ci.yml`:
  - `build-and-test`
  - `version-label-check`
- Branch protection should require both checks.

## Notes

- Script is non-destructive and fast-fails on first error.
- If `.github/workflows` does not exist yet, script exits successfully with a skip message.
