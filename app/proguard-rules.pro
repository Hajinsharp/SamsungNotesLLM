# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# GSON
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# TensorFlow Lite
-keep class org.tensorflow.** { *; }

# MLC LLM
-keep class ai.mlc.** { *; }
-keep interface ai.mlc.** { *; }

# Kotlin
-dontwarn kotlin.random.jdk8.**

# Coroutines
-keep class kotlinx.coroutines.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
