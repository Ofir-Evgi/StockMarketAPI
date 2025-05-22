# 📦 Android SDK Usage

This page explains how to use the Stock Market SDK in your Android app.

## 🛠️ Installation via JitPack

Add this to your root `build.gradle`:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

Then add the dependency in your app/build.gradle:

implementation ("com.github.Ofir-Evgi:StockMarketAPI:1.0.0")
