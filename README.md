# 🧪 DensityBox

**DensityBox** is an intuitive, modern, and high-precision Android application built with Jetpack Compose and Material 3. It provides an all-in-one calculator for **Density**, **Mass**, and **Volume**, complete with unit conversions, formula breakdowns, customizable precision settings, calculation history, and quick preset shortcuts.

---

## ✨ Features

- 📐 **3-in-1 Solver Engine**:
  - Solve **Density**: $D = \frac{M}{V}$
  - Solve **Mass**: $M = D \times V$
  - Solve **Volume**: $V = \frac{M}{D}$
- ⚖️ **Multi-Unit Support & Instant Conversion**:
  - **Mass Units**: Milligram ($mg$), Gram ($g$), Kilogram ($kg$), Metric Ton ($t$), Ounce ($oz$), Pound ($lb$).
  - **Volume Units**: Cubic Millimeter ($mm^3$), Cubic Centimeter ($cm^3$), Milliliter ($mL$), Liter ($L$), Cubic Meter ($m^3$), Cubic Inch ($in^3$), Cubic Foot ($ft^3$), US Fluid Ounce ($US\ fl\ oz$), US Gallon ($US\ gal$).
  - **Density Units**: $g/cm^3$, $kg/m^3$, $g/mL$, $kg/L$, $lb/ft^3$, $lb/in^3$, $oz/in^3$.
- 🧮 **High-Precision Calculation Engine**:
  - Utilizes `BigDecimal` math engine for accurate scientific calculations.
  - Configurable decimal precision (up to 10 decimal places) and multiple rounding modes (`HALF_UP`, `HALF_EVEN`, `FLOOR`, `CEILING`, etc.).
- 📖 **Formula & Step-by-Step Details**:
  - Displays explicit formulas and step-by-step substitution steps for educational and scientific reference.
- ⏱️ **Calculation History**:
  - Local history logging for recent calculations.
  - Re-populate past calculations with a single tap, or clear history when needed.
- 🚀 **Quick Presets & Searchable Unit Pickers**:
  - Handy preset chips for quick values (e.g., standard densities for Steel, Water, etc.).
  - Searchable bottom sheets for fast unit selection.
- 🎨 **Material 3 & Jetpack Compose UI**:
  - Dynamic color themes (Light, Dark, System default).
  - Interactive haptic feedback support and animated bottom navigation.
- 🔒 **100% Offline & Private**:
  - Operates completely offline with zero tracking or network permissions needed.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with **Material 3**
- **Architecture**: MVVM with `StateFlow` and unidirectional data flow
- **Navigation**: Jetpack Navigation Compose
- **Persistence**: SharedPreferences / JSON storage for Settings & History
- **Target SDK**: 37 (Android 16 / Android V)
- **Minimum SDK**: 26 (Android 8.0)

---

## 📂 Project Structure

```
DensityBox/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/densitybox/
│   │   │   │   ├── data/            # Repositories & Data Models (History, Settings)
│   │   │   │   ├── domain/          # Density, Mass, Volume Calculators & Unit Registries
│   │   │   │   ├── ui/              # Compose Screens (Home, History, Settings, About)
│   │   │   │   ├── util/            # Helpers (Clipboard, Haptic Feedback, Share)
│   │   │   │   └── MainActivity.kt
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Unit Tests
│   └── build.gradle.kts
└── build.gradle.kts
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Ladybug or newer recommended)
- **JDK 11** or higher
- **Android SDK** API 37

### Build & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/abdullah-ateeq/DensityBox.git
   cd DensityBox
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle dependencies.
4. Run the app on an Android device or emulator (API 26+).

Or compile via command line:
```bash
./gradlew assembleDebug
```

---

## 🧪 Testing

Run unit tests across calculators, unit registries, parsers, and viewmodel logic:
```bash
./gradlew test
```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👤 Author

**Abdullah Ateeq**
- GitHub: [@abdullah-ateeq](https://github.com/abdullah-ateeq)
