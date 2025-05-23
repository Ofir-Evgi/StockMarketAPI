# 📱 Android SDK Integration

This guide explains how to integrate the **StockMarketAPI Android SDK** into your app, configure it, and use its built-in views.

---

## 📦 Installation

### Step 1: Add JitPack to `repositories`

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add SDK and Dependencies

```groovy
dependencies {
    implementation("com.github.Ofir-Evgi:StockMarketAPI:1.0.0")
    // Required libraries
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
}
```

## 📂 Package Overview

| Package | Purpose |
|---------|---------|
| `com.example.stockmarketsdk.models` | Data models (Stock, Price, etc.) |
| `com.example.stockmarketsdk.views` | Custom UI components |
| `com.example.stockmarketsdk.services` | API + Analytics tracking |
| `com.example.stockmarketsdk` | Core API interface |

## 🧩 SDK Views

### 🔍 StockSearchView

Search box with autocomplete for stock lookup.

```java
stockSearchView.setStockList(allStocks);
stockSearchView.setOnStockSelectedListener(new StockSearchView.OnStockSelectedListener() {
    public void onStockSelected(Stock stock) {
        // Handle selection
    }
    public void onQueryTyped(String text) {
        stockListView.filterByText(text);
    }
});
```

### 📃 StockListView

A scrollable, filterable list of stocks.

```java
stockListView.setStocks(stockList);
stockListView.setOnStockClickListener(stock -> {
    // Show detail or graph
});
```

### 📈 StockGraphView

Interactive line chart showing historical or intraday data.

```java
graphView.setStock(stock); // For intraday prices
graphView.loadHistoryBySymbol("AAPL", "1y"); // 1 year history
```

### 🌍 GlobalIndexView

Displays key global market indices with real-time pricing.

```java
globalIndexView.loadData(); // Automatically fetches and displays
```

### ⚖️ StockComparisonView

Side-by-side view of multiple selected stocks with performance indicators.

```java
comparisonView.addStock(stock);
comparisonView.clear(); // Reset view
```

## 📊 Analytics Integration

All views automatically track:
- Screen views
- Clicks
- Time spent

You can also manually track:

```java
AnalyticsTracker.logStockView(context, "AAPL", userId);
AnalyticsTracker.logScreenView(context, "main_screen", userId);
AnalyticsTracker.logScreenTime(context, "main_screen", 42, userId);
```

## ✅ Permissions

Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🧪 ProGuard Rules

If using minification/obfuscation, add:

```kotlin
-keep class com.example.stockmarketsdk.models.** { *; }
-keep class com.example.stockmarketsdk.services.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
```

## 🧼 Cleanup

For better performance and memory management:

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    AnalyticsTracker.logScreenTime(this, "screen_name", duration, userId);
    comparisonView.clear();
}
```

## 🧠 Additional Resources

For full UI usage examples, see [Android Example Application](example-app.md)
