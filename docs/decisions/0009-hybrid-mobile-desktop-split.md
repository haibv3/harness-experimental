# 0009 Hybrid Mobile-Desktop Split

Date: 2026-06-05

## Status

Accepted

## Context

Decision `0008-cross-platform-stack-flutter.md` chose Flutter (Dart) as a single
codebase for all five target platforms (Android, iOS, macOS, Linux, Windows).

The project owner clarified that mobile and desktop have different stack
priorities and should be decided independently:

- **Mobile** may use native-per-platform (Android Kotlin, iOS Swift) for best
  native feel and team fit, or KMP + Compose Multiplatform for shared logic/UI.
- **Desktop** may use KMP + Compose, Flutter, or native-per-platform, depending
  on binary size, ecosystem, and team constraints.

Locking both surfaces to a single cross-platform stack (Flutter or KMP) removes
meaningful tradeoff decisions that should be made separately for mobile vs
desktop.

## Decision

Supersede decision `0008` and adopt a **hybrid approach**: mobile and desktop
are independent stack decisions, not forced into one cross-platform choice.

Concrete changes:

- Mobile stack is decided by `0010-mobile-stack.md` (deferred).
- Desktop stack is decided by `0011-desktop-stack.md` (deferred).
- The harness supports 1–3 codebases depending on those choices (for example
  native Android + native iOS + KMP desktop = 3 codebases; KMP for all 5 = 1
  codebase).

Decision 0008 status changes to **Superseded** (not Rejected; it was a valid
greenfield choice under different assumptions).

## Alternatives Considered

1. Keep decision 0008 (Flutter for all 5). Rejected: the owner wants mobile and
   desktop to optimize for different concerns.
2. Revert to fully open stack (no decision). Rejected: the hybrid split itself
   is a meaningful architectural decision worth recording.

## Consequences

Positive:

- Mobile and desktop can choose stacks independently, optimizing for their
  constraints.
- Native mobile (Kotlin/Swift) becomes a first-class option without abandoning
  cross-platform desktop.
- Clearer separation of concerns: mobile UX/team fit vs desktop
  packaging/distribution.

Tradeoffs:

- May result in 2–3 codebases instead of 1, increasing maintenance if mobile
  and desktop choose different stacks.
- Shared business logic must use a stack-neutral approach (for example Kotlin
  Multiplatform shared module) or be duplicated if mobile/desktop diverge
  completely.

## Follow-Up

- Decide mobile stack via `0010-mobile-stack.md`.
- Decide desktop stack via `0011-desktop-stack.md`.
- Update `docs/ARCHITECTURE.md` to reflect the hybrid model and the rubrics for
  each surface.
- When both 0010 and 0011 are accepted, scaffold the chosen projects and update
  `app/` + `surfaces/` layout accordingly.
