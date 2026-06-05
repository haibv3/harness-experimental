# android/ — Native Android App (Kotlin + Jetpack Compose + MVI)

This is a placeholder for the native Android codebase. No code is scaffolded yet.

## Stack (Accepted via decision 0010)

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture + **MVI** (Model-View-Intent)
- **Module structure**: **Multi-module** (feature modules + core modules)
- **DI**: Hilt (or Dagger if Hilt is too opinionated)
- **Async**: Coroutines + Flow
- **Annotation processing**: **KSP** (Kotlin Symbol Processing), not KAPT
- **Build**: Gradle Kotlin DSL (`.gradle.kts`)
- **Dependency management**: `libs.versions.toml` (version catalog)

## Typical Structure (from ARCHITECTURE.md)

```
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

## Scaffolding

Real project files, `build.gradle.kts`, `libs.versions.toml`, and module
structure will be created when the first Android feature story enters
implementation. See `docs/ARCHITECTURE.md` for full guidance.

## Shared Logic with Desktop

If business logic needs to be shared with the desktop KMP project (see
`desktop-kmp/`), extract domain/application layers into a KMP shared module that
both Android and desktop depend on.
