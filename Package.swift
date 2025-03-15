// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "NittenappsEncodeEngine",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "NittenappsEncodeEngine",
            targets: ["EncodeEnginePlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "EncodeEnginePlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/EncodeEnginePlugin"),
        .testTarget(
            name: "EncodeEnginePluginTests",
            dependencies: ["EncodeEnginePlugin"],
            path: "ios/Tests/EncodeEnginePluginTests")
    ]
)