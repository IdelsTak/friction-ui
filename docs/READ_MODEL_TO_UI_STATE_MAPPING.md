# Read Model to UI State Mapping

This document defines how canonical read models from `friction-core` are transformed into MVU `Model` state in `friction-ui`.

## Scope

- UI mapping only (no changes to canonical read-model contracts).
- Covers dashboard/list and drill-down/detail state.
- Aligns with `MVU_CONVENTIONS.md` purity and effect-boundary rules.

## Mapping: `FrictionSummary` -> Dashboard/List State

Canonical source fields:

- `frictionId`
- `descriptor`
- `prevalence`
- `intensity`
- `persistence`
- `trend_slope`

Suggested UI state shape (example):

- `FrictionListItemState`
  - `id` (from `frictionId`)
  - `title` (from `descriptor`)
  - `prevalenceValue` (from `prevalence`)
  - `intensityValue` (raw numeric)
  - `persistenceDuration` (raw duration)
  - `trendSlope` (raw numeric)
  - `trendDirection` (derived enum)
  - `intensityBand` (derived enum)
  - `isSelected` (UI-local)

Derived field rules:

- `trendDirection`
  - `UP` when `trendSlope > epsilon`
  - `DOWN` when `trendSlope < -epsilon`
  - `FLAT` otherwise
- `intensityBand`
  - `LOW`, `MEDIUM`, `HIGH` from configured threshold ranges

## Mapping: `ObservationDetail` -> Drill-Down/Detail State

Canonical source fields:

- `observationId`
- `provenance_uri`
- `provenance_timestamp`
- `provenance_authorHandle`
- `content_excerpt`

Suggested UI state shape (example):

- `ObservationDetailItemState`
  - `id` (from `observationId`)
  - `sourceUri` (from `provenance_uri`)
  - `timestamp` (raw instant)
  - `authorLabel` (from `provenance_authorHandle`, fallback applied)
  - `excerpt` (from `content_excerpt`)
  - `displayTime` (derived formatted string)
  - `isExpanded` (UI-local)

Derived field rules:

- `authorLabel`
  - use `provenance_authorHandle` when present
  - fallback to `"unknown"` when missing
- `displayTime`
  - format from `timestamp` at presentation boundary only

## Formatting Boundaries

- Keep raw numeric/time values in `Model` where possible.
- Apply locale-specific formatting near view rendering.
- Do not store presentation-only strings as canonical source of truth when reversible raw value exists.

## Loading, Error, and Empty-State Representation

Suggested top-level UI state sections:

- `listStatus`: `IDLE | LOADING | READY | ERROR | EMPTY`
- `detailStatus`: `IDLE | LOADING | READY | ERROR | EMPTY`
- `errorState`: optional structured error payload (`code`, `message`, `context`)

Rules:

- `LOADING`: show loading indicators; preserve previous stable data where possible.
- `READY`: mapped items available.
- `EMPTY`: request succeeded but no items.
- `ERROR`: request/effect failed; error action updates `errorState`.
- Transitions are driven by explicit actions and pure `update` logic.

## Mapping Invariants

- Mapping functions must be deterministic and side-effect free.
- Canonical IDs (`frictionId`, `observationId`) remain stable keys in UI state.
- Missing mandatory canonical fields should fail fast in effect boundary and dispatch explicit error actions.

## Non-Goals

- This document does not redefine canonical read models from `friction-core`.
- This document does not prescribe JavaFX layout/styling.
