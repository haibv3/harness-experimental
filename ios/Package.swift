// swift-tools-version: 6.0
import PackageDescription

let package = Package(
    name: "HarnessApp",
    platforms: [
        .iOS(.v17)
    ],
    products: [
        .library(
            name: "HarnessApp",
            targets: ["HarnessApp"]
        ),
    ],
    dependencies: [
        // Add Swift Package Manager dependencies here
    ],
    targets: [
        .target(
            name: "HarnessApp",
            dependencies: []
        ),
        .testTarget(
            name: "HarnessAppTests",
            dependencies: ["HarnessApp"]
        ),
    ]
)
