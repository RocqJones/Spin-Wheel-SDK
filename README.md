# SpinWheel SDK

A modular Android Spin Wheel SDK built with Jetpack Compose, clean architecture, and Koin DI.
Fetches remote configuration and image assets, renders a layered animated spin wheel, and persists state via SharedPreferences.

---

## Project Structure

```
SpinWheel/
├── app/                        # Demo host application
├── spinwheel-sdk/              # Reusable Android library (published AAR)
├── core/                       # Models, DTOs, network layer, constants
├── data/                       # Repositories, cache, preferences
├── domain/                     # Spin logic
├── di/                         # Koin module definitions
├── gradle/
│   └── libs.versions.toml      # Version catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Modules

### `:spinwheel-sdk` — Publishable SDK Library
Android library module. Public entry point for consumers. Published as a release AAR.

| File | Purpose |
|---|---|
| `SpinWheel` | Public composable entry point |
| `SpinWheelView` | Internal layered composable — background, wheel, frame, button |
| `SpinWheelViewModel` | MVVM ViewModel — drives loading, spinning, and state |
| `SpinWheelUiState` | Immutable UI state data class |
| `SpinWheelSdk` | Public SDK initializer — `SpinWheelSdk.initialize(context)` |
| `ImageLoader` | `rememberPainterFromFile(File?)` Compose utility |

> Published artifact: `com.spinwheel:spinwheel-sdk:1.0.1`

---

### `:app` — Demo Application
Acts as the host app demonstrating SDK integration.

| File | Purpose |
|---|---|
| `DemoApp` | `Application` subclass — calls `SpinWheelSdk.initialize(this)` |
| `MainActivity` | Entry point — renders `SpinWheelDemoScreen` |
| `SpinWheelDemoScreen` | Demo screen — renders the wheel and displays last result |
| `SpinWheel` | Public SDK composable entry point |
| `SpinWheelView` | Internal layered composable — background, wheel, frame, button |
| `SpinWheelViewModel` | MVVM ViewModel — drives loading, spinning, and state |
| `SpinWheelUiState` | Immutable UI state data class |
| `ViewModelModule` | Koin `viewModel { }` registration |
| `SpinWheelSdk` | Public SDK initializer — `SpinWheelSdk.initialize(context)` |
| `SpinWheelKoin` | Internal Koin bootstrap — wires all modules |
| `CacheModule` | Koin module for cache + prefs (Android context required) |
| `AppConstants` | `CONFIG_URL` and four `ASSET_URL_*` Drive direct-download constants |
| `ImageLoader` | `rememberPainterFromFile(File?)` Compose utility |

---

### `:core` — Shared Models & Network
Pure JVM library. No Android dependencies.

| Package | Contents |
|---|---|
| `config` | `RemoteConfig`, `RotationConfig`, `WheelAssets` — internal domain models |
| `dto` | `WidgetResponse`, `WidgetData`, `WheelAssetsDto` etc. — JSON DTOs with `@Serializable` |
| `dto` | `WidgetResponseMapper` — `WidgetResponse.toRemoteConfig()` extension |
| `network` | `HttpClientProvider` — singleton `OkHttpClient` (15s timeouts) |
| `network` | `ConfigHttpDataSource` — `suspend fun fetchConfigJson(url)` |
| `network` | `AssetHttpDataSource` — `suspend fun downloadBytes(url)` |
| `constants` | `SpinWheelPrefsConstants` — SharedPreferences key constants |

---

### `:data` — Data Layer
Pure JVM library. Repositories, cache, and preferences.

| File | Purpose |
|---|---|
| `ConfigRepository` | Fetch remote config with TTL, cache, and offline fallback |
| `AssetRepository` | Download and cache wheel image assets |
| `normalizeDriveUrl()` | Converts Drive share links → direct-download URLs |
| `SpinWheelCache` | Root cache directory — `spinwheel-cache/` |
| `JsonCache` | Reads/writes `config.json` |
| `AssetCache` | Reads/writes asset files keyed by SHA-256 hash of URL |
| `SpinWheelPrefs` | `SharedPreferences` wrapper for fetch time + last spin index |

---

### `:domain` — Business Logic
Pure JVM library.

| File | Purpose |
|---|---|
| `SpinEngine` | `fun spin(segments, rotationConfig): SpinResult` — pure spin calculation |
| `SpinResult` | `resultIndex`, `targetRotationDegrees`, `durationMs` |
| `WheelRotationConfig` | `duration`, `minimumSpins`, `maximumSpins`, `spinEasing` |

---

### `:di` — Koin Module Definitions
Pure JVM library. Android-safe Koin `single { }` definitions only.

| Module | Provides |
|---|---|
| `NetworkModule` | `OkHttpClient`, `ConfigHttpDataSource`, `AssetHttpDataSource` |
| `RepositoryModule` | `ConfigRepository`, `AssetRepository` |
| `DomainModule` | `SpinEngine` |

> `CacheModule` (needs `androidContext()`) lives in `:app`.

---

## Architecture

```
app (demo)
 └── SpinWheelDemoScreen
       └── SpinWheel              ← SDK public composable
             └── SpinWheelViewModel
                   ├── ConfigRepository  →  core/network  →  OkHttp
                   │       └── JsonCache, SpinWheelPrefs
                   ├── AssetRepository   →  core/network  →  OkHttp
                   │       └── AssetCache
                   └── SpinEngine
```

---

## Config JSON Schema

Hosted at `CONFIG_URL` (Google Drive). Fetched on first launch, cached with TTL.

```json
{
  "data": [{
    "network": {
      "attributes": { "refreshInterval": 300, "cacheExpiration": 3600 },
      "assets": { "host": "..." }
    },
    "wheel": {
      "rotation": { "duration": 2000, "minimumSpins": 3, "maximumSpins": 5 },
      "assets": { "bg": "bg.jpeg", "wheelFrame": "wheel-frame.png", "wheelSpin": "wheel-spin.png", "wheel": "wheel.png" }
    }
  }]
}
```

> Asset URLs are resolved from `AppConstants` — the config host is a placeholder.

---

## Publishing

The `:spinwheel-sdk` module is configured with the `maven-publish` plugin and publishes a release AAR.

### Build the SDK

```bash
./gradlew :spinwheel-sdk:assembleRelease
```

Produces the AAR at:
```
spinwheel-sdk/build/outputs/aar/spinwheel-sdk-release.aar
```

### Publish to Maven Local

```bash
./gradlew :spinwheel-sdk:publishToMavenLocal
```

Publishes the artifact to:
```
~/.m2/repository/com/spinwheel/spinwheel-sdk/1.0.1/
├── spinwheel-sdk-1.0.1.aar
├── spinwheel-sdk-1.0.1.pom
├── spinwheel-sdk-1.0.1.module
└── spinwheel-sdk-1.0.1-sources.jar
```

### Consume from another Android project

Add `mavenLocal()` to the consumer project's repository list, then declare the dependency:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

// build.gradle.kts
dependencies {
    implementation("com.spinwheel:spinwheel-sdk:1.0.1")
}
```

---

## SDK Integration

```kotlin
// 1. Initialize in Application class
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SpinWheelSdk.initialize(this)
    }
}

// 2. Render the composable
SpinWheel(
    configUrl = "https://your-config-url",
    onSpinEnd = { resultIndex ->
        Log.d("Demo", "Result: $resultIndex")
    }
)
```

---

## Dependencies

| Library | Purpose |
|---|---|
| `com.squareup.okhttp3:okhttp` | HTTP networking |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | JSON parsing |
| `io.insert-koin:koin-android` | Dependency injection |
| `io.insert-koin:koin-androidx-compose` | Koin `koinViewModel()` in Compose |
| `androidx.lifecycle:lifecycle-viewmodel-ktx` | ViewModel + `viewModelScope` |
| `androidx.compose.*` | UI framework |
