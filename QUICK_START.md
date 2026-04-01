# Quick Start Guide

**TL;DR - Get running in 5 minutes:**

## Step 1: Open in Android Studio

```bash
# Navigate to project
cd c:\Users\I768631\Personalandroidapp\SamsungNotesLLM

# Open in Android Studio
# File → Open → Select this folder
```

## Step 2: Sync Dependencies

Android Studio will automatically sync Gradle dependencies. Wait for completion.

## Step 3: Build Debug APK

```bash
# Use build.bat (Windows)
build.bat
# Select option 1 (Debug) or 2 (Release)

# Or use Android Studio
# Build → Make Project
```

## Step 4: Connect Tab S9 & Install

```bash
# Ensure USB Debugging is ON on Tab S9
# Device Settings → Developer Options → USB Debugging → ON

# Install via script
build.bat
# Select option 3 (Build and Install)

# Or install manually
adb install app\build\outputs\apk\release\app-release.apk
```

## Step 5: Enable Accessibility Service

On Tab S9:
1. Settings → Accessibility → Downloaded Apps
2. Find "Samsung Notes LLM"
3. Toggle ON

## Step 6: Grant Permissions

On App:
1. Open Samsung Notes LLM app
2. Go to Settings → Permissions
3. Enable: Storage, Accessibility, Network

## Done! 🎉

The app should now:
- Load the Mistral 7B model (first time ~10-30 sec)
- Show "Model Loaded" status
- Accept note content via paste/text input
- Generate responses

---

## File Structure Reference

```
SamsungNotesLLM/
├── app/                                 # Android app module
│   ├── src/main/
│   │   ├── kotlin/com/example/samsungnotes/
│   │   │   ├── MainActivity.kt          ← UI (Jetpack Compose)
│   │   │   ├── accessibility/           ← Samsung Notes integration
│   │   │   ├── llm/                     ← Mistral 7B inference
│   │   │   ├── model/                   ← Data models
│   │   │   └── ui/                      ← ViewModel
│   │   ├── res/                         ← Resources
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts                 ← Dependencies & config
├── README.md                            ← Full documentation
├── IMPLEMENTATION_GUIDE.md              ← Detailed guide
├── QUICK_START.md                       ← This file
├── build.bat                            ← Build script (Windows)
├── build.gradle.kts                     ← Root config
└── settings.gradle.kts                  ← Project settings
```

---

## Common Commands

```bash
# Build
./gradlew assembleRelease       # Release APK
./gradlew assembleDebug         # Debug APK

# Install
adb install app/build/outputs/apk/release/app-release.apk

# View device logs
adb logcat | grep "SamsungNotesLLM"

# Check connectivity
adb devices                     # List connected devices

# Uninstall app
adb uninstall com.example.samsungnotes
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| "Cannot find android.jar" | Ensure Android SDK is installed in Android Studio |
| APK won't install | Check device is in USB Debugging mode |
| Model loads but no response | Replace mock inference in `LLMInferenceService.kt` |
| App crashes on startup | Check device has 6GB+ RAM available |
| Accessibility Service disabled | Re-enable in Settings → Accessibility |

---

## Notes

- First app launch: Model loads (patience required!)
- Responses: Mock until MLC LLM inference is integrated
- Model file: Download from HuggingFace when ready to use actual LLM
- Device: Tab S9 specs are perfect for Mistral 7B

---

Need help? See `README.md` or `IMPLEMENTATION_GUIDE.md` for full details.

Good luck! 🚀
