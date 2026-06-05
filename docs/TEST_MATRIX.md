# Test Matrix

This file maps product behavior to proof.

No product behavior has been defined or implemented yet. Do not mark a row
implemented until tests or validation evidence exist.

## Status Values

| Status | Meaning |
| --- | --- |
| planned | Accepted as intended behavior, not implemented |
| in_progress | Actively being built |
| implemented | Implemented and proof exists |
| changed | Contract changed after earlier implementation |
| retired | No longer part of the product contract |

## Matrix

| Story | Contract | Unit | Integration | E2E | Platform | Status | Evidence |
| --- | --- | --- | --- | --- | --- | --- | --- |
| TBD | Add rows when story packets are created | no | no | no | no | planned | none |

## Evidence Rules

- Unit proof covers pure domain and application rules in shared code.
- Integration proof covers local persistence, sync/API clients, secure storage,
  provider SDK behavior, and presentation/view-model contracts.
- E2E proof covers user-visible app flows driven through the UI on at least one
  target platform (for example Android instrumentation, iOS XCUITest, or a
  desktop UI driver).
- Platform proof covers shell, packaging, signing, OS permissions, deep links,
  notifications, and per-platform behavior that cannot be proven in lower layers.
  Note which platforms the proof actually ran on (Android, iOS, macOS, Linux,
  Windows); untested platforms are unproven, not assumed at parity.
- A story can be implemented without every proof column if the story packet
  explains why, but cross-platform behavior changes must state their platform
  coverage explicitly.
