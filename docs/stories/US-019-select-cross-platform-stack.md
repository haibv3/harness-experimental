# US-019 Select Cross-Platform App Stack

## Status

implemented

## Lane

normal

## Product Contract

Choose the application stack for the project's mobile (Android, iOS) and desktop
(macOS, Linux, Windows) apps, using the rubric in
`docs/decisions/0007-mobile-desktop-app-focus.md`, and record the choice as a
durable decision before any app code is scaffolded.

## Relevant Product Docs

- `docs/decisions/0007-mobile-desktop-app-focus.md`
- `docs/ARCHITECTURE.md`
- `README.md` (Current State, Product Sources)

## Acceptance Criteria

- The nine rubric criteria are each answered for the recommended stack.
- At least two candidate approaches are compared (for example Flutter vs Kotlin
  Multiplatform + Compose), with platform coverage stated for all five targets.
- A follow-up decision record (`docs/decisions/00NN-*.md`) records the chosen
  stack, its consequences, and the `surfaces/*` folder mapping it implies.
- The chosen decision is registered with `scripts/bin/harness-cli decision add`.
- `docs/ARCHITECTURE.md` is updated only where the chosen stack makes a generic
  rule concrete (no premature scaffolding).

## Design Notes

- Commands: none (analysis + decision authoring).
- Queries: `scripts/bin/harness-cli query decisions` to confirm registration.
- API: none.
- Tables: none.
- Domain rules: stack choice must keep business logic in shared inner layers.
- UI surfaces: maps to the `surfaces/` skeleton in `app/` and `surfaces/`.

## Validation

When updating durable proof status, use numeric booleans:
`scripts/bin/harness-cli story update --id US-019 --unit 0 --integration 0 --e2e 0 --platform 0`.

This is a decision/spike story; proof is the registered follow-up decision, not
test layers. Mark proof columns 0 and explain here when closing.

| Layer | Expected proof |
| --- | --- |
| Unit | n/a (no code) |
| Integration | n/a (no code) |
| E2E | n/a (no code) |
| Platform | n/a (no code) |
| Release | n/a (no code) |

## Harness Delta

This story exists because of decision `0007`, which refocused the harness on
mobile + desktop apps and deferred stack choice. Closing it should produce the
stack decision and any first scaffolding story.

## Evidence

- Chosen approach: **Scenario A — Native mobile + KMP desktop** (3 codebases).
- Decisions recorded:
  - `docs/decisions/0009-hybrid-mobile-desktop-split.md` (accepted; supersedes 0008)
  - `docs/decisions/0010-mobile-stack.md` (accepted: **native** — Android Kotlin/Compose/MVI + iOS Swift/SwiftUI)
  - `docs/decisions/0011-desktop-stack.md` (accepted: **KMP + Compose Multiplatform**)
  - `docs/decisions/0008-cross-platform-stack-flutter.md` (superseded by 0009)
- Registered: `scripts/bin/harness-cli query decisions` shows 0009/0010/0011
  (accepted), 0008 (superseded).
- `docs/ARCHITECTURE.md` updated to reflect scenario A: 3 codebases (Android
  native, iOS native, desktop KMP), Clean Architecture, detailed Android stack
  (MVI, multi-module, Hilt, KSP, gradle kts, libs.versions.toml).
- Skeleton restructured: removed generic `app/` + `surfaces/`; created
  `android/`, `ios/`, `desktop-kmp/` placeholders with README guidance.
- No app code scaffolded (deferred to first feature story).

