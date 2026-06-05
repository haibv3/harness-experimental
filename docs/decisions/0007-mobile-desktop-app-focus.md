# 0007 Mobile And Desktop App Focus

Date: 2026-06-05

## Status

Accepted

## Context

Decision `0003-generic-spec-intake-harness.md` made the harness stack-neutral so
it could host any future project. The project owner has now chosen a product
direction: build client applications for mobile (Android, iOS) and desktop
(macOS, Linux, Windows).

The harness docs still described generic, server-leaning shapes (HTTP request
logs, browser E2E, backend integration) that do not match cross-platform app
work. Agents needed architecture, intake, and validation guidance tuned for
apps: deep links, OS permissions, secure storage, signing, store release, and
platform parity.

The concrete application stack is not yet decided. Picking a stack now would be
premature; the harness principle is to choose a stack only when a story needs
it. This decision records the focus and the selection rubric while keeping the
stack deferred.

## Decision

Refocus the harness on cross-platform mobile and desktop application
development, and specialize the policy docs accordingly, while leaving the
application stack open.

Specialized in this change:

- `docs/ARCHITECTURE.md`: target platforms, shared-vs-platform layering,
  app-oriented parse-first boundaries, and a client telemetry observability
  contract.
- `docs/FEATURE_INTAKE.md`: app-oriented risk flags (OS permissions, native
  capability, release/distribution, offline/sync) and hard gates.
- `docs/TEST_MATRIX.md`: proof tiers and platform-coverage rules for apps.
- `README.md`: current state and product sources reflect the app focus.

Deferred until the stack-selection story:

- The concrete UI framework, language, local persistence, and packaging.
- Real app scaffolding, build config, and per-platform project files.

This decision does not supersede `0003`. The harness remains reusable; it is now
specialized for one product class (apps) rather than carrying a project-specific
spec.

## Stack-Selection Rubric

The stack-selection story must choose an approach against these criteria:

1. Platform coverage: must cover all five targets (Android, iOS, macOS, Linux,
   Windows) or explicitly justify any gap.
2. Codebase model: single shared codebase vs native-per-platform, and how much
   product logic is shared.
3. UI consistency vs native feel: tradeoff the product can accept.
4. Language and team fit: existing skills and hiring reality.
5. Ecosystem maturity: tooling, libraries, CI, signing, and store support.
6. Performance and binary/app size constraints.
7. Testing story: unit, integration, UI/E2E, and per-platform proof support.
8. Distribution and update: store submission, notarization, and auto-update.
9. Long-term maintenance and community/vendor risk.

Candidate approaches to evaluate (non-binding):

- Single codebase: Flutter (Dart) or Kotlin Multiplatform + Compose Multiplatform.
- Web-shell hybrid: Tauri or similar with a shared web UI.
- Native per platform: Kotlin/Android, Swift/iOS+macOS, native desktop shells.

## Alternatives Considered

1. Lock a specific stack now. Rejected: violates the choose-stack-when-needed
   principle and removes a meaningful decision from its own story.
2. Keep the harness fully generic. Rejected: the owner committed to an app
   product class, and generic server-leaning guidance was actively misleading
   for app work.
3. Scaffold app code immediately. Rejected: scaffolding requires a concrete
   stack, which is deferred.

## Consequences

Positive:

- Architecture, intake, and validation guidance now match app development.
- The stack decision is preserved as an explicit, reviewable story.
- Agents get app-specific risk and proof expectations before writing code.

Tradeoffs:

- The harness is less generic than after `0003`.
- Some guidance (concrete folders, build config) stays abstract until a stack is
  chosen.

## Follow-Up

- Create the stack-selection story and choose a stack with this rubric.
- When the stack is chosen, record a follow-up decision and create real
  scaffolding only as the first feature story needs it.
