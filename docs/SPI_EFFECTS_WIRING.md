# SPI Consumption and Effects Wiring

This document defines UI-side consumption of canonical SPI/read-model outputs and how effects are wired without violating MVU purity.

## Scope

- Applies to `friction-ui` effect wiring and reactive integration.
- Complements canonical SPI contracts in `friction-core`.
- Must align with `MVU_CONVENTIONS.md` (`update` stays pure).

## Action Flow: Input -> Update

1. External input arrives from SPI/read-model source (initial load, refresh, push update).
2. Effect layer transforms external payload into explicit UI actions.
3. Actions are dispatched to `update(model, action)`.
4. `update` returns next `Model` plus optional effect command descriptors.
5. Effect executor runs commands and dispatches completion/failure actions.

Example action sequence:

- `LoadRequested`
- `LoadSucceeded(FrictionSummary[])`
- `FrictionSelected(frictionId)`
- `ObservationLoadRequested(frictionId)`
- `ObservationLoadSucceeded(ObservationDetail[])`
- `ObservationLoadFailed(error)`

## Effect Boundary and Execution Model

- Effect commands are created by `update` as data, not executed inside `update`.
- Effect executor is the only place allowed to perform IO/network/reactive subscription side effects.
- Effect results must always return as explicit actions (success/failure/cancel).
- Keep effect handlers small and operation-specific; avoid manager-style god objects.

## RxJava/RxJavaFX Scheduler Policy

- IO/network and heavy transformation work: background scheduler (`Schedulers.io()` or compute scheduler by policy).
- UI rendering state dispatch: JavaFX scheduler / UI-thread dispatch boundary.
- Scheduler use must be explicit in chain composition; avoid implicit thread hops.
- Long-lived streams must define backpressure strategy and cancellation path.

## Subscription and Disposal Ownership

- Ownership of each subscription must be explicit and lifecycle-bound.
- Use composite/disposable containers at screen/session scope.
- Dispose subscriptions on view/model lifecycle termination to prevent leaks.
- Re-subscribe only through explicit actions (for example on retry or reload), not hidden side effects.

## Error Propagation into Explicit Actions

- Map every failure to explicit action types; do not swallow exceptions.
- Error actions must carry actionable context:
  - operation
  - reason category (transient/terminal)
  - optional correlation/request id
- `update` maps error actions into deterministic `Model` error state.

Example:

- effect failure -> `LoadFailed(errorContext)`
- `update` -> sets `listStatus=ERROR` and stores `errorState`

## Purity Guardrails

- `update` must never create subscriptions, call network APIs, or touch IO.
- Views must never call SPI directly; they dispatch intent actions only.
- Reactive chains should emit deterministic action sequences for the same input conditions.

## Testing Expectations

- Unit tests:
  - verify action -> model transitions remain pure
  - verify effect command emission from `update`
- Effect-layer tests:
  - verify success/failure mapping into explicit actions
  - verify scheduler/disposal behavior with deterministic time/test scheduler where possible
- TestFX:
  - verify user interactions dispatch expected actions and render resulting model states

## Non-Goals

- This document does not redefine canonical SPI contracts from `friction-core`.
- This document does not prescribe JavaFX view layout/style decisions.
