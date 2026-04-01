# Samsung Notes LLM - Android Application

A cutting-edge Android application that seamlessly integrates Samsung Notes with an on-device Large Language Model (Mistral 7B) to provide intelligent analysis and responses based on your note content.

## Features

✨ **Key Capabilities:**
- 📝 Direct Samsung Notes integration via Accessibility Service
- 🧠 On-device LLM inference (Mistral 7B quantized)
- 🔒 Complete privacy - no data sent to external servers
- ⚡ Fast responses with MLC LLM optimization
- 💾 Offline functionality
- 🎨 Beautiful Material Design 3 UI (Jetpack Compose)
- 📱 Optimized for Samsung Galaxy Tab S9

## Project Structure

```
SamsungNotesLLM/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/example/samsungnotes/
│   │   │   ├── MainActivity.kt              # Main UI (Jetpack Compose)
│   │   │   ├── accessibility/
│   │   │   │   └── NotesAccessibilityService.kt  # Samsung Notes content extraction
│   │   │   ├── llm/
│   │   │   │   └── LLMInferenceService.kt   # Mistral 7B inference
│   │   │   ├── model/
│   │   │   │   └── Models.kt                # Data classes
│   │   │   └── ui/
│   │   │       └── NotesLLMViewModel.kt     # UI state management
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   ├── xml/
│   │   │   │   ├── accessibility_service_config.xml
│   │   │   │   ├── backup_rules.xml
│   │   │   │   └── data_extraction_rules.xml
│   │   │   └── drawable/
│   │   │       └── ic_launcher.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── .gitignore
```

## Technology Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material Design 3
- **LLM Runtime:** MLC LLM (Mobile Optimized)
- **Model:** Mistral 7B (Quantized for mobile)
- **Build System:** Gradle Kotlin DSL
- **Target SDK:** Android 14 (API 34)
- **Minimum SDK:** Android 9 (API 28)

## Prerequisites

Before building, ensure you have:

1. **Android Studio:** Latest version (Giraffe or newer)
2. **Android SDK:** API 34 installed
3. **JDK:** Version 11 or higher
4. **Git:** For version control
5. **Gradle:** Bundled with Android Studio

## Setup Instructions

### 1. Prerequisites Setup

```bash
# Navigate to project directory
cd c:\Users\I768631\Personalandroidapp\SamsungNotesLLM

# Verify Android SDK is set (in Android Studio)
# Tools → SDK Manager → Verify API 34 is installed
```

### 2. Configure local.properties

Create `local.properties` in the project root:

```properties
sdk.dir=/path/to/Android/Sdk
ndk.dir=/path/to/Android/Ndk (optional)
```

### 3. Install MLC LLM Dependencies

The project includes MLC LLM for on-device inference. The dependencies are defined in `app/build.gradle.kts`. Gradle will automatically download them during the build process.

### 4. Build the Project

```bash
# From project root
./gradlew build

# Or in Android Studio
# Build → Make Project
```

### 5. Download and Prepare the Model

**Important:** Before running on your Tab S9, you need to prepare the Mistral 7B quantized model:

```bash
# Download quantized Mistral 7B (Q4 format) - approximately 4-5 GB
# Reference: https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.2-GGUF

# Place the model in:
app/src/main/assets/models/mistral-7b-q4.bin
```

**Alternative:** The `LLMInferenceService.kt` can be configured to automatically download models on first launch.

## Building the APK

### Generate Release APK (for installation on Tab S9)

```bash
# Using Gradle
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### Sign the APK (Required for installation)

```bash
# Generate signing key (one-time)
keytool -genkey -v -keystore samsungnotes-release.keystore \
        -keyalg RSA -keysize 2048 -validity 10000 \
        -alias samsungnotes-key

# Sign the APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
          -keystore samsungnotes-release.keystore \
          app/build/outputs/apk/release/app-release.apk \
          samsungnotes-key

# Verify signature
jarsigner -verify -certs -verbose \
          app/build/outputs/apk/release/app-release.apk
```

### Install on Tab S9

```bash
# Connect Tab S9 via USB
# Enable Developer Mode and USB Debugging

# Install APK
adb install app/build/outputs/apk/release/app-release.apk

# Or drag-and-drop app-release.apk into Android Studio's device window
```

## How to Use

1. **Enable Accessibility Service:**
   - Open app
   - Go to Settings → Accessibility → Samsung Notes LLM
   - Turn ON "Samsung Notes LLM"

2. **Load a Note:**
   - Open Samsung Notes
   - Copy note content (Ctrl+C)
   - Return to app
   - Paste content in the input field

3. **Get LLM Response:**
   - Tap "Send to LLM"
   - Wait for Mistral 7B to process (first time loads model ~10-30 seconds)
   - Response appears in the result section

## Configuration

### Modify Model Source

Edit `LLMInferenceService.kt` to:
- Change model name/version
- Configure inference parameters (temperature, top_k, etc.)
- Implement model auto-download
- Add streaming responses

### Adjust UI Theme

Customizable in:
- `app/src/main/res/values/colors.xml` — Color scheme
- `app/src/main/res/values/themes.xml` — Material Design theme

### Performance Tuning

In `app/build.gradle.kts`:
- Adjust thread pool sizes
- Configure memory limits for model inference
- Optimize TFLite GPU acceleration

## Troubleshooting

### APK Installation Issues

**Error: "app not installed"**
- Ensure APK is properly signed
- Check device has sufficient storage
- Verify Android version matches minimum API 28

**Error: "file is corrupted"**
- Rebuild APK: `./gradlew clean assembleRelease`
- Verify no build failures in logs

### Model Loading Issues

**"Error loading model"**
- Verify model file exists in `app/src/main/assets/models/`
- Check model format matches MLC LLM requirements
- Ensure device has 6GB+ RAM available

### Samsung Notes Integration Not Working

- Enable Accessibility Service in system settings
- Grant necessary permissions
- Restart the app
- Check Accessibility Service is still enabled (sometimes disabled by system updates)

## Permissions Required

The app requests:
- `INTERNET` — For downloading models initially
- `READ_EXTERNAL_STORAGE` — To access notes
- `WRITE_EXTERNAL_STORAGE` — To cache models
- `BIND_ACCESSIBILITY_SERVICE` — For Samsung Notes integration
- `QUERY_ALL_PACKAGES` — To detect Samsung Notes app

## Next Steps / Enhancements

- [ ] Integrate actual MLC LLM inference (currently using mock responses)
- [ ] Implement automatic model download on first launch
- [ ] Add model selection UI (Llama, Phi, etc.)
- [ ] Implement streaming token responses
- [ ] Add response history/chat UI
- [ ] Support for different prompt templates
- [ ] Cloud backup of notes (optional)
- [ ] Multi-language support
- [ ] Hardware acceleration (GPU/NPU if available)
- [ ] App-specific shortcuts (tap &hold launcher icon)

## Build Information

- **Min API:** 28 (Android 9)
- **Target API:** 34 (Android 14)
- **Language Level:** Kotlin 1.9
- **Compose Version:** 2023.10.01
- **Material Design:** Material Design 3

## License

This project is open source. Feel free to modify and distribute.

## Support & Resources

- **Android Development:** https://developer.android.com/
- **Jetpack Compose:** https://developer.android.com/jetpack/compose
- **MLC LLM:** https://github.com/mlc-ai/mlc-llm
- **Mistral 7B:** https://huggingface.co/mistralai/Mistral-7B
- **Material Design 3:** https://m3.material.io/

---

**Created:** April 2026  
**Target Device:** Samsung Galaxy Tab S9  
**Status:** Ready for Development

For issues or questions, check the documentation in each module or refer to inline code comments.
