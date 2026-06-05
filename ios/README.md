# ios/ — Native iOS App (Swift + SwiftUI)

This is a placeholder for the native iOS codebase. No code is scaffolded yet.

## Stack (Accepted via decision 0010)

- **Language**: Swift
- **UI**: SwiftUI
- **Architecture**: Clean Architecture (adapt MV pattern or TCA as needed)
- **Async**: async/await + Combine (or Swift Concurrency)
- **DI**: manual DI or a lightweight container (Swinject, etc.)
- **Build**: Xcode project + Swift Package Manager for dependencies

## Typical Structure (from ARCHITECTURE.md)

```
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

## Scaffolding

Real Xcode project, Swift Package Manager dependencies, and module structure
will be created when the first iOS feature story enters implementation. See
`docs/ARCHITECTURE.md` for full guidance.

## Shared Logic with Desktop

iOS uses Swift; desktop uses Kotlin (KMP). If business logic needs to be shared,
consider a KMP shared module with Swift interop (via Kotlin/Native) or accept
duplication and keep iOS logic in Swift.
