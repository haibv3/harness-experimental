# desktop-kmp/ — Desktop App (KMP + Compose Multiplatform)

This is a placeholder for the Kotlin Multiplatform + Compose Multiplatform
desktop codebase (macOS, Linux, Windows). No code is scaffolded yet.

## Stack (Accepted via decision 0011)

- **Language**: Kotlin (Kotlin Multiplatform)
- **UI**: Compose Multiplatform (JVM target)
- **Architecture**: Clean Architecture in KMP common
- **Packaging**: `jpackage` or Conveyor for native installers (bundles JRE)
- **Build**: Gradle Kotlin DSL + Compose Multiplatform plugin
- **Distribution**: DMG (macOS), MSI/exe (Windows), deb/AppImage (Linux)

## Typical Structure (from ARCHITECTURE.md)

```
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

## Scaffolding

Real KMP project, `build.gradle.kts`, Compose Multiplatform plugin config, and
module structure will be created when the first desktop feature story enters
implementation. See `docs/ARCHITECTURE.md` for full guidance.

## Shared Logic with Android

Android is native Kotlin; desktop is KMP. Extract domain/application layers into
the KMP `shared/commonMain/` module so both Android and desktop can depend on
it. Android-specific code stays in `android/`, desktop-specific in
`desktop-kmp/desktopMain/`.
