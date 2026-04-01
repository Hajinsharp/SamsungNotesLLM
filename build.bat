@echo off
REM Samsung Notes LLM - Build & Deploy Script for Windows
REM This script builds the APK and installs it on your connected Samsung Tab S9

setlocal enabledelayedexpansion

echo ============================================
echo Samsung Notes LLM - Build & Install Script
echo ============================================
echo.

REM Check if gradle wrapper exists
if not exist "gradlew.bat" (
    echo Error: gradlew.bat not found! Run from project root directory.
    exit /b 1
)

REM Menu
echo Select an option:
echo 1. Build Debug APK
echo 2. Build Release APK
echo 3. Build and Install (requires ADB)
echo 4. Install existing APK via ADB
echo 5. Clean build
echo 6. Run tests
echo 7. Exit
echo.

set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" goto build_debug
if "%choice%"=="2" goto build_release
if "%choice%"=="3" goto build_install
if "%choice%"=="4" goto install_only
if "%choice%"=="5" goto clean_build
if "%choice%"=="6" goto run_tests
if "%choice%"=="7" goto end
goto invalid

:build_debug
echo.
echo Building Debug APK...
call gradlew.bat assembleDebug
if %ERRORLEVEL% EQU 0 (
    echo.
    echo BUILD SUCCESSFUL!
    echo APK location: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo BUILD FAILED! Check logs above.
)
goto end

:build_release
echo.
echo Building Release APK (optimized)...
call gradlew.bat assembleRelease
if %ERRORLEVEL% EQU 0 (
    echo.
    echo BUILD SUCCESSFUL!
    echo APK location: app\build\outputs\apk\release\app-release.apk
    echo.
    echo To install: adb install app\build\outputs\apk\release\app-release.apk
) else (
    echo.
    echo BUILD FAILED! Check logs above.
)
goto end

:build_install
echo.
echo Building Release APK and installing...
call gradlew.bat assembleRelease
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful! Installing to device...
    adb install -r app\build\outputs\apk\release\app-release.apk
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo INSTALLATION SUCCESSFUL!
        echo The app should appear on your device shortly.
    ) else (
        echo.
        echo INSTALLATION FAILED!
        echo Make sure Tab S9 is connected and USB Debugging is enabled.
    )
) else (
    echo.
    echo BUILD FAILED! Cannot install.
)
goto end

:install_only
echo.
echo Checking for APK...
if exist "app\build\outputs\apk\release\app-release.apk" (
    echo Installing APK...
    adb install -r app\build\outputs\apk\release\app-release.apk
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo INSTALLATION SUCCESSFUL!
    ) else (
        echo.
        echo INSTALLATION FAILED!
        echo Make sure Tab S9 is connected with USB Debugging enabled.
        echo Connect tab S9: adb connect DEVICE_IP:5555
    )
) else (
    echo.
    echo APK not found! Build a Release APK first (option 2).
)
goto end

:clean_build
echo.
echo Cleaning build artifacts...
call gradlew.bat clean
echo Build cleaned.
goto end

:run_tests
echo.
echo Running tests...
call gradlew.bat test
goto end

:invalid
echo.
echo Invalid choice. Please select 1-7.
goto end

:end
echo.
echo ============================================
echo Done!
echo ============================================
pause
