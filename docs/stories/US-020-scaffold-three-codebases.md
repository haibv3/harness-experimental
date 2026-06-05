# US-020 Scaffold Three Codebases

## Status

in_progress

## Lane

normal

## Product Contract

Create the initial project scaffolding for all three codebases (Android, iOS,
desktop-kmp) with build configuration, dependency management, and minimal
structure following the stacks chosen in decisions 0010 and 0011. No runnable
app yet — just the build files, module structure, and base classes needed for
the first feature implementation.

## Relevant Product Docs

- `docs/decisions/0010-mobile-stack.md` (Android: Kotlin/Compose/MVI/multi-module/Hilt/KSP; iOS: Swift/SwiftUI)
- `docs/decisions/0011-desktop-stack.md` (desktop: KMP + Compose Multiplatform)
- `docs/ARCHITECTURE.md` (Clean Architecture, 3 codebase structure)

## Acceptance Criteria

### Android
- Multi-module Gradle project structure (`app/`, `core/core-ui/`, `core/core-domain/`)
- Root `build.gradle.kts` + `settings.gradle.kts` + `gradle.properties`
- `libs.versions.toml` with centralized dependency versions (Compose, Hilt, KSP, Coroutines, etc.)
- Hilt setup in `app` module with `@HiltAndroidApp` application class
- KSP plugin configured (not KAPT)
- MVI base classes in `core-ui` (`Intent`, `Model`, `ViewState` sealed classes/interfaces)
- Minimal `MainActivity` with Compose `setContent`

### iOS
- Xcode project structure (`App.xcodeproj`, `App/`, `Core/`)
- Swift Package Manager `Package.swift` (if using SPM for dependencies)
- Minimal `App.swift` entry point with SwiftUI `@main`
- Placeholder `ContentView.swift`

### Desktop KMP
- KMP Gradle project with `shared/` module (`commonMain/`, `desktopMain/`)
- `desktop/` Compose Desktop app module
- Root `build.gradle.kts` + `settings.gradle.kts` for KMP
- Minimal `Main.kt` in `desktop/` with Compose Desktop `application { }` block
- Placeholder Compose UI in `shared/commonMain/`

## Design Notes

- **No SDK/toolchain present**: scaffold files only; actual build/run deferred to
  developer with proper environment.
- **No feature code**: just structure + build config + base classes.
- **Android**: Use latest stable Compose BOM, Hilt 2.5x, Kotlin 2.x, KSP.
- **iOS**: Use latest stable Swift/SwiftUI (6.x).
- **Desktop KMP**: Use Compose Multiplatform 1.7+, Kotlin 2.x.

## Validation

When updating durable proof status, use numeric booleans:
`scripts/bin/harness-cli story update --id US-020 --unit 0 --integration 0 --e2e 0 --platform 0`.

This is a scaffolding story; proof is the existence of valid build files, not
running tests. Mark proof columns 0 and explain here when closing.

| Layer | Expected proof |
| --- | --- |
| Unit | n/a (no feature code yet) |
| Integration | n/a (no feature code yet) |
| E2E | n/a (no feature code yet) |
| Platform | File existence check: `build.gradle.kts`, `libs.versions.toml`, `App.xcodeproj`, KMP `settings.gradle.kts` all present |
| Release | n/a (no signing/distribution yet) |

## Harness Delta

This story creates the concrete project scaffolding deferred since US-019. After
this, the first feature story can add real domain/UI code and run builds.

## Evidence

All three codebases scaffolded successfully:

### Android (15 files created)
- Multi-module structure: `app/`, `core/core-ui/`, `core/core-domain/`, `core/core-data/`
- Root build files: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
- `gradle/libs.versions.toml` with centralized dependencies (Compose BOM, Hilt 2.54, KSP, Kotlin 2.0.21)
- Hilt setup: `@HiltAndroidApp` in `HarnessApplication.kt`
- KSP plugin configured in `app/build.gradle.kts`
- MVI base classes in `core-ui`: `MviIntent`, `MviViewState`, `MviViewModel`
- Minimal `MainActivity` with Compose + `HarnessTheme`

### iOS (3 Swift files + Package.swift)
- Structure: `App/`, `Core/{Domain,Data,UI}/`, `Features/`
- Swift Package Manager: `Package.swift` (Swift 6.0, iOS 17+)
- Entry point: `HarnessApp.swift` with `@main`
- Placeholder UI: `ContentView.swift` with SwiftUI

### Desktop KMP (6 files created)
- KMP shared module: `shared/src/{commonMain,desktopMain}/kotlin/`
- Compose Desktop app: `desktop/src/main/kotlin/`
- Root build files: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
- Shared logic placeholder: `Greeting.kt` in `commonMain`
- Desktop entry point: `Main.kt` with Compose Desktop `application { }`
- Native distribution config: DMG/MSI/Deb in `desktop/build.gradle.kts`

No SDK/toolchain present; scaffold files only. Actual build/run deferred to developer with proper environment.
