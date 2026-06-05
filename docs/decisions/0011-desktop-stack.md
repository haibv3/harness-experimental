# 0011 Desktop Stack

Date: 2026-06-05

## Status

Accepted

## Context

Decision `0009-hybrid-mobile-desktop-split.md` separated mobile and desktop into
independent stack decisions. This decision covers the desktop surface (macOS,
Linux, Windows).

Three approaches are viable:

1. **KMP + Compose Multiplatform**: Kotlin codebase, Compose UI, runs on JVM
   (bundles a JRE). Can share business logic with mobile if mobile also chooses
   KMP (via 0010).
2. **Flutter**: Dart codebase, self-rendered UI, AOT-compiled binaries (no JRE).
   Mature desktop support (stable on all three platforms).
3. **Native per platform**: macOS (Swift + AppKit/SwiftUI), Linux (C++/Rust +
   GTK/Qt), Windows (C#/.NET + WinUI or C++ + Win32). Best native integration,
   highest maintenance cost.

The choice depends on binary size constraints, shared-logic reuse with mobile,
ecosystem maturity, and team language fit.

## Decision

The desktop stack is **Kotlin Multiplatform (KMP) + Compose Multiplatform** for
all three desktop platforms (macOS, Linux, Windows).

This choice enables:

- Shared business logic with mobile via a KMP common module (mobile is native
  Kotlin/Swift per `0010-mobile-stack.md`, so Android can share Kotlin code).
- One Kotlin codebase for desktop UI and logic.
- JVM packaging for all three platforms (bundles JRE, larger binaries but
  simpler distribution than native per platform).

Tradeoff: larger binary size vs Flutter (which is AOT-compiled), but better
shared-logic story with Android.

## Desktop Stack Rubric

| # | Criterion | KMP + Compose | Flutter | Native per platform |
| --- | --- | --- | --- | --- |
| 1 | Platform coverage | All 3 (JVM) | All 3 (stable) | All 3 (separate codebases) |
| 2 | Codebase count | 1 Kotlin | 1 Dart | 3 (macOS/Linux/Windows) |
| 3 | Shared logic with mobile | Yes (if mobile = KMP) | No (unless via FFI) | Possible via KMP shared module |
| 4 | Binary size / dependencies | Larger (bundles JRE ~50–100MB) | Smaller (AOT, no runtime) | Smallest (native only) |
| 5 | Native UI feel | Compose-rendered (less native) | Self-rendered (less native) | Best (platform-native widgets) |
| 6 | Ecosystem maturity | Growing (JetBrains-backed) | Mature (Google-backed) | Mature (platform first-party) |
| 7 | Packaging / distribution | JVM packaging (jpackage, etc.) | flutter build (exe/dmg/AppImage) | Per-platform (Xcode, MSI, deb/rpm) |
| 8 | Auto-update | Custom or Sparkle/WinSparkle | Custom (no built-in) | Custom per platform |
| 9 | Team language fit | Kotlin (shares with Android if native mobile) | Dart (new language) | Swift/C++/C# (platform specialists) |

## KMP + Compose Desktop Stack (if chosen)

When KMP + Compose Multiplatform is selected for desktop:

- **Language**: Kotlin
- **UI**: Compose for Desktop (Compose Multiplatform JVM target)
- **Architecture**: Clean Architecture in KMP common (can share with mobile if
  mobile = KMP)
- **Packaging**: `jpackage` or Conveyor for native installers (bundles JRE)
- **Build**: Gradle Kotlin DSL + Compose Multiplatform plugin
- **Distribution**: DMG (macOS), MSI/exe (Windows), deb/AppImage (Linux)

Typical structure (if sharing with KMP mobile):

```
shared/              # KMP common (domain, application, infrastructure)
  commonMain/
  androidMain/
  iosMain/
  desktopMain/       # desktop-specific adapters
android/             # Android app shell
ios/                 # iOS app shell
desktop/             # Compose Desktop app shell
```

## Flutter Desktop Stack (if chosen)

When Flutter is selected for desktop:

- **Language**: Dart
- **UI**: Flutter widgets (self-rendered)
- **Architecture**: Clean Architecture in Dart (separate from mobile if mobile =
  native/KMP)
- **Packaging**: `flutter build macos/windows/linux`
- **Build**: `pubspec.yaml` + Flutter SDK
- **Distribution**: DMG (macOS), MSIX/exe (Windows), AppImage/snap (Linux)

Typical structure:

```
lib/
  domain/
  application/
  infrastructure/
  presentation/
macos/               # macOS runner
windows/             # Windows runner
linux/               # Linux runner
```

## Native Desktop Stack (if chosen)

When native per platform is selected:

- **macOS**: Swift + AppKit or SwiftUI, Xcode, DMG distribution
- **Linux**: C++/Rust + GTK or Qt, CMake/Cargo, deb/rpm/AppImage
- **Windows**: C#/.NET + WinUI 3 or C++ + Win32, Visual Studio, MSI/MSIX

This approach is justified only when:

- Binary size must be minimal (no JRE, no Flutter engine).
- Native platform integration (for example macOS menu bar, Windows system tray,
  Linux D-Bus) is a hard requirement.
- The team has platform specialists and can maintain 3 codebases.

## Alternatives Considered

1. Electron or Tauri (web-based desktop). Not prioritized; the project owner did
   not list web as a target surface.
2. .NET MAUI. Not considered; assumes Windows-first or Xamarin legacy, not a
   greenfield cross-platform choice.

## Consequences

Positive (when decided):

- Clear stack guidance for desktop implementation.
- Team can optimize for binary size (Flutter/native), shared logic (KMP), or
  ecosystem maturity.

Tradeoffs (deferred state):

- No desktop scaffolding or concrete architecture until this decision is
  accepted.
- First desktop feature story must choose and record the outcome here.

## Follow-Up

- When a project or team context is known, evaluate the rubric and update this
  decision's status to **Accepted** with the chosen stack.
- Scaffold the chosen desktop project(s) and update `surfaces/desktop/` layout.
- If both mobile (0010) and desktop choose KMP, structure the repo as a KMP
  multiplatform project with shared common code.
