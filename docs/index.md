# 📘 StockMarketAPI Documentation

Welcome to the official documentation for the **StockMarketAPI** project.

This site provides complete guides and references for:

- 🧩 [Android SDK Integration](sdk.md)
- 🌐 [Backend API Service](api.md)
- 📱 [Android Example Application](example-app.md)

---

## 📂 Repository Overview

StockMarketAPI/
├── app/ # Example Android app
├── stockmarketsdk/ # Android SDK library
├── backend/ # Flask API and MongoDB connection
├── docs/ # GitHub Pages documentation
├── LICENSE # MIT License
└── README.md # Project overview and setup

yaml
Copy
Edit

---

## 🔗 Useful Links

- 🌐 Live API: [https://stockmarketapi-qr65.onrender.com](https://stockmarketapi-qr65.onrender.com)
- 📦 JitPack SDK: `com.github.Ofir-Evgi:StockMarketAPI:1.0.0`
- 📁 MongoDB Atlas: Remote cloud database
- 🎛️ Admin Dashboard: Included as optional web module

---

## 🚀 Getting Started with StockMarketAPI

This guide will help you quickly integrate the StockMarketAPI SDK into your Android application.

## 📦 Installation

### 1. Add JitPack Repository

Add the JitPack repository to your project's `settings.gradle.kts`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add SDK Dependency

Add the SDK dependency to your app's `build.gradle.kts`:

```groovy
dependencies {
    implementation("com.github.Ofir-Evgi:StockMarketAPI:1.0.0")

    // Required dependencies
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
}
```

### 3. Add Required Permissions

Add these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 📱 Quick Start

### 1. Initialize SDK

In your `Application` class or `MainActivity`:

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // SDK initialization if needed
    }
}
```

### 2. Add a View

Choose the view you want to use:

```java
// Stock Selection View
StockMarketSDK.attachStockSelectionView(context, container);

// Stock List View
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachStockListView(context, container, symbols, 5000);

// Global Indices View
StockMarketSDK.attachGlobalIndexView(context, container);

// Stock Comparison View
StockMarketSDK.attachStockComparisonView(context, container);
```

### 3. Cleanup

Always detach the SDK when done:

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    StockMarketSDK.detach();
}
```

## 🔧 Configuration

### System Requirements

- Android SDK 26 (Android 8.0)
- Java 11
- Internet connection

### ProGuard Rules

If using code obfuscation, add these rules:

```
-keep class com.example.stockmarketsdk.models.** { *; }
-keep class com.example.stockmarketsdk.services.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
```

## 📊 Analytics

The SDK automatically tracks:

- Screen views
- Stock selections
- Search queries
- View duration
- User interactions

## 🔍 Troubleshooting

### Common Issues

1. **View Not Updating**

   - Check internet connection
   - Verify refresh interval
   - Check resource cleanup

2. **Memory Leaks**

   - Ensure `detach()` is called
   - Check lifecycle management
   - Verify handler cleanup

3. **Network Issues**
   - Check permissions
   - Verify endpoint
   - Check internet connection

## 📚 Next Steps

- [SDK Documentation](sdk.md) - Complete SDK guide
- [API Documentation](api.md) - Backend API details
- [Example App Guide](example-app.md) - Sample implementation

## 🆘 Need Help?

- Check the [troubleshooting guide](sdk.md#troubleshooting)
- Review the [example app](example-app.md)
- Contact: [evgiofir1@gmail.com](mailto:evgiofir1@gmail.com)

---

## 🛠 Sections in This Documentation

- 📱 [Android SDK](sdk.md): Installation, views, analytics
- 🌐 [Backend API](api.md): Endpoints, CRUD, MongoDB schema
- 📱 [Example App](example-app.md): Activities and integration examples

---

_This site is generated with GitHub Pages and Markdown from the `/docs` folder._
