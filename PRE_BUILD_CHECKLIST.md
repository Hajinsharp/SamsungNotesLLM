# Pre-Build Checklist

## ✅ Environment Setup

Before building the APK, verify you have:

### System Requirements
- [ ] Windows 10/11 (or MacOS/Linux)
- [ ] 8GB+ RAM available
- [ ] 10GB+ free disk space (for Android SDK + models)
- [ ] Stable internet connection

### Software Installation
- [ ] **Android Studio** (Latest version, Giraffe 2022.3.1 or newer)
  - Download: https://developer.android.com/studio
  - [ ] Opened and configured
  - [ ] Accept license agreements
  
- [ ] **Android SDK**
  - [ ] SDK Platform API 34 installed
  - [ ] Build Tools 34.0.0+ installed
  - [ ] Android Emulator (optional, for testing)
  - [ ] Location: Typically `C:\Users\%USERNAME%\AppData\Local\Android\Sdk`

- [ ] **JDK 11+**
  - [ ] Bundled with Android Studio (recommended)
  - Or: Download from https://jdk.java.net/

- [ ] **Git** (optional but recommended)
  - Download: https://git-scm.com/
  - For version control and updates

### Device Setup (Galaxy Tab S9)
- [ ] Device connected via USB-C cable
- [ ] USB Debugging enabled:
  - Settings → Developer Options → USB Debugging → ON
  - (Enable Developer Options: Settings → About → Build Number → Tap 7 times)
- [ ] USB authorization allowed on device
- [ ] Device screen unlocked during installation
- [ ] Minimum 5GB free storage on device

---

## ✅ Project Configuration

### 1. Set Android SDK Path

Create `local.properties` in project root:

```properties
# Location: c:\Users\I768631\Personalandroidapp\SamsungNotesLLM\local.properties
sdk.dir=C:\\Users\\%USERNAME%\\AppData\\Local\\Android\\Sdk
```

(Replace %USERNAME% with your actual Windows username)

### 2. Verify Gradle Wrapper

These files should already exist in the project:
- [ ] `gradlew` (Unix/Mac executable)
- [ ] `gradlew.bat` (Windows batch file)
- [ ] `gradle/wrapper/gradle-wrapper.jar`
- [ ] `gradle/wrapper/gradle-wrapper.properties`

If missing, Android Studio will generate them automatically.

### 3. Dependencies are Pre-configured

No additional setup needed! The following are already in `app/build.gradle.kts`:
- ✅ Jetpack Compose
- ✅ Material Design 3
- ✅ Kotlin Coroutines
- ✅ MLC LLM
- ✅ TensorFlow Lite
- ✅ Retrofit + OkHttp
- ✅ Room Database

---

## ✅ Build Pre-flight Checks

### Before Running Build:

1. [ ] All files created successfully
   ```bash
   # From project root, verify these exist:
   dir /s app\src\main\kotlin\com\example\samsungnotes\
   dir /s app\src\main\res\
   ```

2. [ ] No syntax errors in Kotlin files
   - Android Studio will show red underlines if there are issues
   - Hover over errors to see suggestions

3. [ ] Network connection available
   - Gradle needs to download ~2GB of dependencies
   - First build will take 10-15 minutes

4. [ ] No conflicting Android apps installed
   - Uninstall any previous builds of this app:
   ```bash
   adb uninstall com.example.samsungnotes
   ```

---

## ✅ Build Steps

### Option A: Using Build Script (Easiest)

```bash
cd c:\Users\I768631\Personalandroidapp\SamsungNotesLLM
build.bat
# Select option 2 (Release APK) or 3 (Build & Install)
```

### Option B: Using Android Studio UI

1. Open AndroidStudio
2. File → Open → Select SamsungNotesLLM folder
3. Wait for Gradle sync (bottom status bar)
4. Build → Build Bundle(s) / APK(s) → Build APK
5. Monitor build progress in Build window (View → Tool Windows → Build)

### Option C: Using Gradle Command

```bash
cd c:\Users\I768631\Personalandroidapp\SamsungNotesLLM
./gradlew assembleRelease
```

---

## ✅ Installation Steps

### Prerequisites Before Install:
- [ ] APK successfully built (no errors)
- [ ] Tab S9 connected via USB
- [ ] USB Debugging enabled on device
- [ ] Sufficient storage on device (500MB+)

### Installation Method A: Using Script

```bash
# From project root
build.bat
# Select option 3 (Build and Install)
# Or option 4 (Install existing APK)
```

### Installation Method B: Using ADB

```bash
adb devices  # Verify device is connected
adb install -r app\build\outputs\apk\release\app-release.apk
```

### Installation Method C: Manual (via File Transfer)

1. Connect device
2. Allow file access on device
3. Transfer `app\build\outputs\apk\release\app-release.apk` to device
4. Open file manager on device
5. Locate APK file
6. Tap to install
7. Grant permissions when prompted

---

## ✅ Post-Installation

### On Your Tab S9:

1. [ ] App appears in app drawer
   - Swipe up from bottom to see all apps
   - Look for "Samsung Notes LLM"

2. [ ] App opens without crashing
   - Tap to launch

3. [ ] Grant required permissions
   - Storage
   - Accessibility Service
   - Network (Internet)

4. [ ] Enable Accessibility Service
   - Settings → Accessibility → Apps → Samsung Notes LLM → Enable

5. [ ] Model loads successfully
   - App should show "Model Loaded" status
   - First load takes ~10-30 seconds

---

## ✅ Verification Checklist

### App Launch Test:
- [ ] App opens without errors
- [ ] UI displays correctly
- [ ] "Model Status" card shows
- [ ] "Send to LLM" button is visible

### Feature Tests:
- [ ] Can paste text in input field
- [ ] "Clear" button works
- [ ] "Send to LLM" button responds
- [ ] Response appears (even if it's mock response)

### Accessibility Service (Optional):
- [ ] Open Samsung Notes app
- [ ] Write some text
- [ ] Return to Samsung Notes LLM app
- [ ] Service should detect content

---

## ✅ Troubleshooting During Build

### Gradle Sync Fails
```
Error: "Failed to resolve dependency..."
```
**Solution:**
```bash
# Clear cache and try again
./gradlew clean
./gradlew build --refresh-dependencies
```

### Cannot Find android.jar
```
Error: "Cannot find android.jar"
```
**Solution:**
1. Open Android Studio
2. Tools → SDK Manager
3. Ensure API 34 Platform is installed
4. Click Sync Now

### Out of Memory
```
Error: "OutOfMemoryError"
```
**Solution:**
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
```

### Long Build Times (First Build)
This is normal! First build:
- Downloads ~2GB of dependencies
- Compiles Kotlin code
- Generates resources
- Can take 15-20 minutes

---

## ✅ Troubleshooting Installation

### APK Installation Fails
```
Error: "app not installed"
```
**Solution:**
1. Ensure USB Debugging is ON on device
2. Unlock device screen
3. Uninstall previous version: `adb uninstall com.example.samsungnotes`
4. Try again: `adb install app\build\outputs\apk\release\app-release.apk`

### Cannot Find Device
```
Error: "no devices found"
```
**Solution:**
```bash
# Check connections
adb devices

# Authorize device if needed
# Check device screen for authorization prompt
# Select "Always trust this computer"

# Or reconnect:
adb disconnect
adb connect DEVICE_IP:5555  # If using wireless
```

### Insufficient Storage
```
Error: "insufficient storage"
```
**Solution:**
1. On device, free up space
   - Settings → Storage → Manage Storage
   - Delete unused apps/media
2. Retry installation

---

## Final Checklist Before You Start

- [ ] I have Android Studio installed
- [ ] I have Android SDK API 34 installed
- [ ] My Tab S9 is connected via USB
- [ ] USB Debugging is enabled on Tab S9
- [ ] I'm in the correct project directory
- [ ] I have internet connection (for downloading dependencies)
- [ ] I have 10GB+ free disk space

## Ready to Build? 🚀

```bash
# Navigate to project
cd c:\Users\I768631\Personalandroidapp\SamsungNotesLLM

# Build using script
build.bat

# Or manually
./gradlew assembleRelease
adb install app\build\outputs\apk\release\app-release.apk
```

If you encounter any issues, check the detailed guides:
- `README.md` — Full documentation
- `IMPLEMENTATION_GUIDE.md` — Phase-by-phase implementation
- `QUICK_START.md` — Quick reference

---

**Estimated Time:**
- First build: 15-20 minutes
- Installation: 1-2 minutes
- Post-setup: 5 minutes

**Total: ~30 minutes** from start to running app on your Tab S9!

Good luck! 🎉
