# 0010 Mobile Stack

Date: 2026-06-05

## Status

Accepted

## Context

Decision `0009-hybrid-mobile-desktop-split.md` separated mobile and desktop into
independent stack decisions. This decision covers the mobile surface (Android
and iOS).

Two approaches are viable:

1. **Native per platform**: Android (Kotlin + Jetpack Compose) and iOS (Swift +
   SwiftUI) as separate codebases, optimized for native feel, team language fit,
   and platform-specific tooling.
2. **Cross-platform shared**: Kotlin Multiplatform (KMP) + Compose Multiplatform
   for both Android and iOS, sharing business logic and UI in one Kotlin
   codebase.

The choice depends on team skills, native UX requirements, and whether shared
mobile logic justifies the KMP learning curve.

## Decision

The mobile stack is **native per platform**:

- **Android**: Kotlin + Jetpack Compose + MVI + multi-module + Hilt + KSP +
  Gradle Kotlin DSL + `libs.versions.toml`
- **iOS**: Swift + SwiftUI

This choice optimizes for native feel, platform-specific tooling, and team
language fit. Business logic may be shared via a KMP shared module if desktop
also uses KMP (see `0011-desktop-stack.md`).

## Mobile Stack Rubric

| # | Criterion | Native (Kotlin + Swift) | KMP + Compose |
| --- | --- | --- | --- |
| 1 | Team language fit | Kotlin for Android devs, Swift for iOS devs | Kotlin for both (Swift interop for iOS-only APIs) |
| 2 | Native UI fidelity | Best (Jetpack Compose + SwiftUI native) | Good (Compose Multiplatform renders natively on iOS since 1.8) |
| 3 | Codebase count | 2 (Android + iOS separate) | 1 (shared Kotlin) |
| 4 | Shared business logic | Duplicated or via KMP shared module | Fully shared in KMP common |
| 5 | Ecosystem maturity | Mature (Google + Apple first-party) | Growing fast (JetBrains + Google KMP stable, Compose iOS stable 2025) |
| 6 | Platform-specific APIs | Direct access (no bridge) | Kotlin/Native + expect/actual or Swift interop |
| 7 | Testing | Separate per platform (Espresso, XCTest) | Shared Kotlin tests + platform UI tests |
| 8 | Hiring / onboarding | Separate Android/iOS specialists | Kotlin devs can cover both |
| 9 | Long-term maintenance | 2 codebases to maintain | 1 codebase, but KMP/Compose updates affect both |

## Native Android Stack (if chosen)

When native Android is selected, use this stack:

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture + **MVI** (Model-View-Intent)
- **Module structure**: **Multi-module** (feature modules + core/shared modules)
- **DI**: Hilt (or Dagger if Hilt is too opinionated)
- **Async**: Coroutines + Flow
- **Annotation processing**: **KSP** (Kotlin Symbol Processing), not KAPT
- **Build**: Gradle Kotlin DSL (`.gradle.kts`)
- **Dependency management**: `libs.versions.toml` (version catalog)

Typical module breakdown (adapt per project):

```
android/
  app/               # app module (wiring, navigation host)
  feature/
    feature-a/       # feature module (MVI: intent → model → view)
    feature-b/
  core/
    core-domain/     # shared domain entities, use cases
    core-data/       # repositories, data sources
    core-ui/         # shared Compose components, theme
    core-network/    # API clients
    core-database/   # local persistence (Room, etc.)
```

## Native iOS Stack (if chosen)

When native iOS is selected, use this stack:

- **Language**: Swift
- **UI**: SwiftUI
- **Architecture**: Clean Architecture (adapt MV pattern or TCA as needed)
- **Async**: async/await + Combine (or Swift Concurrency)
- **DI**: manual DI or a lightweight container (Swinject, etc.)
- **Build**: Xcode project + Swift Package Manager for dependencies

## KMP + Compose Stack (if chosen)

When KMP + Compose Multiplatform is selected:

- **Language**: Kotlin (common + Android/iOS targets)
- **UI**: Compose Multiplatform (shared UI across Android + iOS)
- **Architecture**: Clean Architecture in KMP common; MVI or similar in shared presentation
- **Module structure**: KMP shared module + Android/iOS app shells
- **DI**: Koin (KMP-friendly) or manual DI
- **Async**: Coroutines + Flow (KMP common)
- **Build**: Gradle Kotlin DSL + KMP plugin; Xcode for iOS runner

## Alternatives Considered

1. Flutter for mobile. Rejected under the hybrid model (0009); Flutter is a
   candidate for desktop (0011), not mobile.
2. React Native. Not considered; team context assumes Kotlin/Swift preference.

## Consequences

Positive (when decided):

- Clear stack guidance for mobile implementation.
- Team can optimize for native feel (native) or shared codebase (KMP).

Tradeoffs (deferred state):

- No mobile scaffolding or concrete architecture until this decision is accepted.
- First mobile feature story must choose and record the outcome here.

## Follow-Up

- When a project or team context is known, evaluate the rubric and update this
  decision's status to **Accepted** with the chosen stack.
- Scaffold the chosen mobile project(s) and update `surfaces/mobile/` layout.
- If native is chosen and desktop also chooses native (via 0011), consider a KMP
  shared module for business logic to avoid full duplication.
