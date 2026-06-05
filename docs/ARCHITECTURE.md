# Architecture

This project is focused on building client applications for mobile and desktop:

- Mobile: Android and iOS.
- Desktop: macOS, Linux, and Windows.

The application follows a **hybrid approach**: mobile and desktop use independent
stacks optimized for their constraints. See:

- `docs/decisions/0009-hybrid-mobile-desktop-split.md` (supersedes 0008)
- `docs/decisions/0010-mobile-stack.md` (accepted: **native** — Android Kotlin/Compose/MVI + iOS Swift/SwiftUI)
- `docs/decisions/0011-desktop-stack.md` (accepted: **KMP + Compose Multiplatform**)

The project has **three codebases**:

1. **Android** (native Kotlin, Jetpack Compose, MVI, multi-module, Hilt, KSP)
2. **iOS** (native Swift, SwiftUI)
3. **Desktop** (KMP + Compose Multiplatform for macOS/Linux/Windows)

Business logic may be shared between Android and desktop via a KMP shared module.
No application code exists yet. This document defines architecture questions and
boundary rules for the chosen stacks. Real scaffolding (project files, build
config, signing) lands with the first feature story that needs a runnable app.

## Target Platforms

| Surface | Platforms | Typical shell concerns |
| --- | --- | --- |
| Mobile | Android, iOS | App lifecycle, permissions, push, deep links, background limits, store review |
| Desktop | macOS, Linux, Windows | Windowing, menus, file system, auto-update, code signing/notarization, packaging |

Every product surface above shares product logic but has its own shell,
packaging, signing, and release path. Treat shared logic as the inner layers and
each platform shell as an outer surface.

## Discovery Before Shape

Before proposing implementation shape, identify:

- Product surfaces in scope: which of the five target platforms this work
  touches, and whether behavior must stay at parity across them.
- Runtime stack: language, UI framework, local persistence, sync/backend,
  providers, and packaging/distribution per platform.
- Core domains: the product concepts that deserve stable names and contracts.
- Boundary inputs: user input, deep links and app links, OS intents/IPC, push
  payloads, file imports, camera/sensor data, credentials and secure storage,
  remote API responses, and per-platform configuration.
- Offline and sync posture: what works offline, what is cached, and how
  conflicts resolve when connectivity returns.
- Validation ladder: the smallest checks that can prove shared logic plus the
  platform behavior that cannot be proven in shared code.

Record stack and packaging choices in `docs/decisions/` when they meaningfully
constrain future work.

## Default Layering (Clean Architecture)

All stacks (native, KMP, Flutter) follow Clean Architecture layering:

```text
domain
  <- application
      <- infrastructure
          <- presentation
              <- platform shells
```

Inner layers hold product truth and must not depend on outer layers. Each
platform shell stays as thin as the stack allows: lifecycle wiring, navigation
host, permission prompts, packaging, and signing.

## Project Structure (Native Mobile + KMP Desktop)

The project has three codebases following Clean Architecture:

### Android (Native Kotlin + Jetpack Compose + MVI)

```text
android/
  app/                      # App module (navigation host, DI setup)
  feature/
    feature-auth/           # Feature module: authentication (MVI)
      src/main/kotlin/
        ui/                 # Compose UI + MVI intent/model/view
        domain/             # Feature-specific use cases
        data/               # Feature-specific repositories
    feature-dashboard/
  core/
    core-domain/            # Shared domain entities, use cases
    core-data/              # Shared repositories, data sources
    core-ui/                # Shared Compose components, theme, MVI base
    core-network/           # API clients (Retrofit, Ktor, etc.)
    core-database/          # Local persistence (Room, SQLDelight, etc.)
  build.gradle.kts
  libs.versions.toml        # Centralized dependency versions
```

**Stack**: Kotlin, Jetpack Compose, MVI, multi-module, Hilt (DI), Coroutines +
Flow, KSP (not KAPT), Gradle Kotlin DSL.

### iOS (Native Swift + SwiftUI)

```text
ios/
  App/                      # App target (navigation, DI)
  Features/
    Auth/                   # Feature module: authentication
    Dashboard/
  Core/
    Domain/                 # Shared domain entities, use cases
    Data/                   # Shared repositories, data sources
    UI/                     # Shared SwiftUI components, theme
    Network/                # API clients
    Database/               # Local persistence (CoreData, Realm, etc.)
  App.xcodeproj
```

**Stack**: Swift, SwiftUI, async/await + Combine, manual DI or Swinject, Swift
Package Manager.

### Desktop (KMP + Compose Multiplatform)

```text
desktop-kmp/
  shared/                   # KMP shared module
    commonMain/kotlin/
      domain/               # Shared domain (can reuse from Android if extracted)
      application/          # Shared use cases
      infrastructure/       # Shared data layer
    desktopMain/kotlin/     # Desktop-specific adapters
  desktop/                  # Compose Desktop app shell
    src/main/kotlin/
      ui/                   # Compose Desktop UI
      Main.kt               # Desktop entry point
  build.gradle.kts
```

**Stack**: Kotlin Multiplatform, Compose Multiplatform (JVM), Gradle Kotlin DSL.
Can share a KMP `shared` module with Android for domain/application layers.

This is a thinking template, not a scaffold. Create real folders, build config,
and signing setup when the first feature story enters implementation.

## Shared-vs-Platform Rule

Keep behavior in the highest shared layer that can express it.

| Concern | Default home | Notes |
| --- | --- | --- |
| Business rules, validation, entities | domain | Shared via KMP if mobile+desktop both use KMP; duplicated if stacks diverge |
| Use cases, orchestration | application | Same as domain |
| Local persistence, sync, secure storage | infrastructure | Stack-specific adapters; KMP can share interfaces |
| Presentation state, navigation, deep-link mapping | presentation | Shared if UI stack is shared (KMP Compose or Flutter); separate if native |
| Permission prompts, OS dialogs, lifecycle, packaging, signing | platform shell | Always platform-specific |

Platform-specific code is justified only when the OS forces it (capability APIs,
store rules, signing, native UX conventions). Duplicated business logic across
shells is a defect, not parity. If mobile and desktop choose different stacks,
consider a KMP shared module for domain/application to avoid full duplication.

## Dependency Rule

Inner layers must not depend on outer layers.

| Layer | May depend on | Must not depend on |
| --- | --- | --- |
| domain | nothing project-external except tiny pure utilities | framework, database, UI, provider, OS APIs, process/env |
| application | domain | framework, UI, provider, persistence concrete clients |
| infrastructure | domain, application | presentation state or platform shell assumptions |
| presentation | domain, application contracts | concrete OS APIs, a single platform's shell |
| platform shells | presentation, infrastructure adapters | domain internals directly |

## Parse-First Boundary Rule

Unknown data must be parsed at boundaries before it enters inner code.

Boundaries include:

- Remote API responses, params, and query strings.
- Session payloads, identity claims, and tokens from secure storage.
- Deep links, app links, universal links, and custom URL schemes.
- OS intents, IPC messages, and inter-app share payloads.
- Push notification and background-message payloads.
- Imported files, clipboard data, camera/scanner, and sensor input.
- Local database/cache rows and serialized persisted state.
- Remote config, feature flags, and per-platform environment configuration.

Target flow:

```text
unknown input
  -> parser
  -> typed DTO or command
  -> application use case
  -> domain object/value object
```

Inner layers should work with meaningful product types such as `UserId`,
`AccountId`, `WorkspaceId`, `Role`, `DateRange`, or domain-specific IDs,
rather than repeatedly validating raw strings.

## Command/Query Boundary

If the product has both reads and writes, keep command/query separation clear at
the code level even when the storage layer is simple:

- Commands mutate state and own audit side effects.
- Queries read state and format for consumers.
- Shared domain rules live in domain/application, not controllers.

## Observability Contract

Client apps observe sessions and events, not server requests. The app should
emit structured client telemetry with a stable schema per event:

- timestamp
- level
- session_id
- user_id when known and consented
- platform and app_version
- screen or action
- duration_ms when measuring a flow
- outcome (success, error, cancelled)
- message

Additional client-app requirements:

- Capture unhandled crashes and ANRs with a symbolicated report path.
- Respect privacy and consent: no PII or secrets in telemetry; gate analytics
  behind user consent where the platform or law requires it.
- Distinguish product analytics (what users do) from operational diagnostics
  (crashes, performance). Do not use one as a substitute for the other.
- Telemetry must degrade gracefully offline and flush when connectivity returns.

If a backend exists, the server keeps the canonical one-line-per-request log
described for services; this contract covers the app client.
