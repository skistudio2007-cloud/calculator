# Calcora — Premium AMOLED Scientific Calculator & Universal Converter

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Kotlin-2.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/about/versions/nougat)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-brightgreen.svg)](https://developer.android.com/)
[![Privacy](https://img.shields.io/badge/Privacy-100%25%20Offline%20%26%20Zero--Telemetry-success.svg)](#100-user-safety--privacy-guarantee)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

**Calcora** is a precision-engineered, luxury AMOLED scientific calculator, financial utility, and high-accuracy universal unit converter built natively for Android using Jetpack Compose and Material Design 3.

---

## Highlights & Key Features

### 1. Pure AMOLED Scientific Calculator
- **Minimalist Clean Header**: Quick navigation (`History`, `Tools`) on the left, full overflow menu `(⋮)` on the top right.
- **Dedicated Scientific Keypad Toggle**: Integrated toggle button (`[√ π e =]`) directly above the keypad to switch seamlessly between standard 4-column and advanced 5-column scientific functions.
- **Haptic Tactile Feedback**: Ultra-fast, subtle tactile vibrations with user toggle in Settings.
- **Smart History**: Auto-saves expressions with timestamp, tap-to-reuse, and one-tap clear.

### 2. Universal Any-to-Any High-Accuracy Unit Converter
- **Exact Mathematical Precision**: Powered by high-precision `BigDecimal` arithmetic up to 10 decimal digits. Eliminates truncation or rounding down of micro-units (e.g., Data: `1 MB` to `TB` or `PB`).
- **Universal Dropdowns & Swap (`⇄`)**: Any unit to any unit selection within each category.
- **Live Equivalents**: Real-time breakdown table showing input converted simultaneously into every other unit in the category.
- **15 Complete Categories**: Data/Storage, Length, Mass, Area, Volume, Speed, Time, Temperature, Pressure, Force, Power, Energy, Frequency, Angle, Fuel.

### 3. Comprehensive Financial & Daily Utilities
- **EMI / Loan Calculator** (Principal, Rate, Tenure, Monthly Breakdown)
- **GST / Sales Tax Calculator** (Add / Remove Tax)
- **Discount Calculator**
- **Currency Converter**
- **Split Bill & Tip Calculator**
- **BMI & Health Evaluator**
- **Date Difference & Age Calculator**

---

## 100% User Safety & Privacy Guarantee

Calcora is built with strict privacy-by-design standards:
- **Zero Telemetry / Zero Tracking**: No analytics SDKs (Firebase, Mixpanel, Google Analytics, etc.).
- **Zero Ads**: No ad banners, interstitials, or tracking identifiers.
- **100% Offline**: All calculations, conversions, and historical records remain strictly on the local device.
- **No Internet Permission Required**: Calcora functions completely offline and does not request network permissions.
- **Single Minimal Permission**: Only `android.permission.VIBRATE` is requested to provide tactile haptic button responses.

---

## Release & Installation

The app is packaged and signed for direct installation:

| Artifact | File Location | Purpose |
|---|---|---|
| **Direct Install APK** | `app/build/outputs/apk/release/app-release.apk` | Ready to transfer and install on any Android phone (Android 7.0+) |
| **Google Play Bundle (AAB)** | `app/build/outputs/bundle/release/app-release.aab` | Optimized Android App Bundle for Play Store publication |
| **Web Preview** | `index.html` (or run `open_in_browser.bat`) | Full-featured responsive web preview |

### How to Install on Phone
1. Copy `app-release.apk` to your Android device (via USB cable, Google Drive, WhatsApp, Telegram, etc.).
2. Tap the file in your device's File Manager and choose **Install**.
3. If prompted, allow "Install from unknown sources" for your file browser.

---

## Building from Source

```bash
# Build Release APK
./gradlew assembleRelease

# Build Google Play App Bundle
./gradlew bundleRelease

# Run Unit Tests
./gradlew testDebugUnitTest
```

---

## License
Released under the MIT License.
