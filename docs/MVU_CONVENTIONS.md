# MVU Conventions

This document defines UI-owned Model-View-Update conventions for `friction-ui`.

## Scope

These rules apply to UI state management and view orchestration in this repo only.
They align with Friction's DDD and EO direction where applicable to UI code.

## State Ownership and Boundaries

- `Model` is the single source of truth for UI state.
- Views must render from `Model` only; views do not own mutable state.
- Domain contracts from `friction-core` are consumed as inputs and mapped into UI-specific view state.
- Keep transient UI concerns (selection, focus, loading flags, dialog visibility) in `Model`, not in view classes.
- Keep state immutable by default and favor replacing state snapshots over mutating fields.
- Keep data visibility narrow; only expose what rendering and update decisions require.

## Action Naming and Intent

- Use action names that express user intent or system event, not implementation details.
- Prefer explicit action names such as:
  - `LoadRequested`
  - `FrictionSelected`
  - `FilterUpdated`
  - `RefreshCompleted`
- Avoid names tied to widget mechanics (for example: `ButtonClicked123`).
- Keep each action focused on one intent.
- Avoid flag-driven behavior in action handling; prefer distinct actions for distinct intent.

## Pure Update Logic Rules

- `update(model, action)` must be deterministic and side-effect free.
- Given the same `model` and `action`, `update` must produce the same next state.
- No IO, no network calls, no clipboard/file operations inside pure update logic.
- Prefer returning:
  - next `Model`
  - optional effect description to be executed outside `update`
- Keep one source of truth for each decision rule; do not duplicate business decisions across view and update layers.
- Reject invalid transition inputs early at boundaries and map them to explicit error actions.

## Side-Effects Boundary

- Side-effects belong in a dedicated effect handling layer, not in `update` and not in views.
- Allowed side-effects include:
  - API calls
  - file/clipboard interactions
  - scheduling/async work
- Effect handlers may dispatch follow-up actions when work completes or fails.
- Failures should map to explicit actions (for example: `RefreshFailed`) and reflected in `Model`.
- Keep side-effects at system edges only (UI events, IO, network boundaries).
- Make retry behavior explicit and bounded (max attempts + backoff) for recoverable external failures.
- Do not swallow exceptions; enrich with context and emit failure actions.

## Reactive Integration (RxJava / RxJavaFX)

- Use RxJava/RxJavaFX in effect handling and UI event bridge layers, not inside pure `update`.
- Stream outputs should be mapped to explicit actions before they reach `update`.
- Scheduler policy must be explicit (UI thread for rendering updates, background schedulers for IO work).
- Subscription/disposal ownership must be explicit and lifecycle-bound to avoid leaks.
- Reactive chains should preserve determinism at MVU boundaries: same emitted action sequence must produce predictable model transitions.

## Object Design Notes for UI Layer

- Favor explicit object collaboration over manager-style procedural classes.
- Prefer composition/decorators for behavior extension over inheritance and branching.
- Keep classes small and cohesive with one UI responsibility.
- Avoid hidden globals and singletons unless lifecycle-managed and justified by the app container.

## Testing Expectations

### Unit Tests

- Unit-test `update` as pure state transitions.
- Use table-style tests for action sequences and expected next model.
- Cover success, failure, and edge actions (empty data, invalid selection, retries).
- Treat tests as the coverage gate for every behavior change in update logic.
- Keep tests isolated and deterministic; avoid network dependencies and use explicit timeouts for async tests.

### TestFX Touchpoints

- Verify view wiring dispatches expected actions.
- Verify visible UI state reflects `Model` updates.
- Cover key user paths: loading, selection, filtering, and error states.
- Keep assertions focused on one behavioral expectation per test case where practical.

## Non-Goals

- This doc does not redefine shared domain contracts from `friction-core`.
- This doc does not prescribe JavaFX layout/styling patterns.
