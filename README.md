# Spin Wheel SDK

A modular Android Spin Wheel SDK with clean architecture.

## Project Structure

```
SpinWheel/
├── app/                          # Sample application module
│   ├── build.gradle.kts
│   └── src/
│       ├── androidTest/
│       │   └── java/com/jonesmbindyo/spinwheel/
│       │       └── ExampleInstrumentedTest.kt
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/jonesmbindyo/spinwheel/
│       │   │   ├── MainActivity.kt
│       │   │   └── ui/theme/
│       │   │       ├── Color.kt
│       │   │       └── ...
│       │   └── res/
│       │       ├── drawable/
│       │       ├── mipmap-*/
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml/
│       └── test/
│           └── java/com/jonesmbindyo/spinwheel/
│               └── ExampleUnitTest.kt
│
├── core/                         # Core utilities and base classes
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/core/
│       ├── MyClass.kt
│       ├── cache/
│       ├── model/
│       ├── network/
│       └── prefs/
│
├── data/                         # Data layer (repositories, data sources)
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/data/
│       ├── MyClass.kt
│       ├── assets/
│       └── config/
│
├── domain/                       # Domain layer (use cases, business logic)
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/domain/
│       ├── MyClass.kt
│       └── wheel/
│
├── ui/                          # UI components and presentation layer
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/ui/
│       └── MyClass.kt
│
├── sdk/                         # Public SDK module
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/sdk/
│       ├── MyClass.kt
│       └── spinwheel/
│
├── di/                          # Dependency injection module
│   ├── build.gradle.kts
│   └── src/main/java/com/jonesmbindyo/di/
│       └── MyClass.kt
│
├── gradle/                      # Gradle configuration
│   ├── gradle-daemon-jvm.properties
│   ├── libs.versions.toml
│   └── wrapper/
│
├── build.gradle.kts             # Root build configuration
├── settings.gradle.kts          # Project settings
├── gradle.properties            # Gradle properties
├── gradlew                      # Gradle wrapper (Unix)
├── gradlew.bat                  # Gradle wrapper (Windows)
├── local.properties             # Local SDK configuration
└── README.md                    # This file
```

## Module Overview

### :app
Sample Android application demonstrating the Spin Wheel SDK usage.

### :core
Core utilities, base classes, and common functionalities:
- Cache management
- Model definitions
- Network utilities
- Preferences handling

### :data
Data layer implementation:
- Repositories
- Data sources
- Asset management
- Configuration

### :domain
Domain layer with business logic:
- Use cases
- Business rules
- Wheel-related domain logic

### :ui
UI components and presentation layer:
- Composable functions
- View models
- UI utilities

### :sdk
Public SDK module exposing the Spin Wheel functionality to external consumers.

### :di
Dependency injection configuration and modules.

## Architecture

This project follows Clean Architecture principles with clear separation of concerns:

```
┌─────────────────────────────────────┐
│           Presentation              │
│         (app, ui modules)           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│            Domain                   │
│      (domain module)                │
│   - Use Cases                       │
│   - Business Logic                  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│             Data                    │
│       (data module)                 │
│   - Repositories                    │
│   - Data Sources                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│             Core                    │
│       (core module)                 │
│   - Common Utilities                │
│   - Base Classes                    │
└─────────────────────────────────────┘
```

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle
4. Run the `:app` module to see the sample implementation

## License

[Add your license here]

