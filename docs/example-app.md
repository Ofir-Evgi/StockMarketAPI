# 📱 Android Example Application

This page documents the official Android example app bundled with the **StockMarketAPI SDK**. It demonstrates how to use the SDK views, connect to the API, and visualize stock market data effectively.

## 🧽 Overview

The sample application includes:

* 🔍 **Search View** with autocomplete (StockSearchView)
* 📃 **Stock List View** with mini-charts (StockListView)
* 📈 **Graph Activity** with MPAndroidChart (StockGraphView)
* ⚖️ **Stock Comparison** tool (StockComparisonView)
* 🌍 **Global Market Indices** section (GlobalIndexView)
* 📊 **Analytics Tracking** (screen time, clicks, stock views)

## 📂 Project Structure

```
app/
├── MainActivity.java
├── StockGraphActivity.java
├── StockComparisonActivity.java
├── GlobalIndicesActivity.java
├── StockDetailActivity.java
├── StockAdapter.java
```

## 🗄️ Screens

| Screen | Description |
|--------|-------------|
| 🔍 **Main Screen** | Search + Stock List + Compare Stocks |
| 📈 **Graph** | Intraday performance of selected stock |
| ⚖️ **Compare** | Add 2+ stocks to compare performance |
| 🌍 **Indices** | View real-time global market indices |
| 🧠 **Details** | Historical performance (1y range) |

## ⚙️ How It Works

### 📌 `MainActivity.java`

* Binds `StockSearchView` and `StockListView`
* Fetches stock data via `StockService.getStocks()`
* Tracks user behavior with `AnalyticsTracker`

```java
StockSearchView searchView = findViewById(R.id.stockSearchView);
StockListView listView = findViewById(R.id.stockListView);

searchView.setStockList(allStocks);
listView.setStocks(allStocks);
```

### 📌 `StockGraphActivity.java`

Displays filtered intraday prices using:

```java
graphView.setStock(stock); // Show prices for today
```

Also tracks screen time automatically.

## 📈 Stock Comparison Flow

From the main screen:

1. Select at least two stocks
2. Tap "Compare"
3. Launches `StockComparisonActivity`
4. Renders comparison view and `LineChart`

```java
comparisonView.addStock(stock);
addStockToChart(stock); // renders dataset
```

## 🌍 Global Indices Flow

* Loads major global indices
* Uses `GlobalIndexView.loadData()`
* Auto-highlights price changes with color

```java
globalIndexView.loadData();
```

## 🧠 Best Practices

* ✅ Track all user actions with `AnalyticsTracker`
* ✅ Clean up views in `onDestroy()`
* ✅ Use proper filtering for live search
* ✅ Use serializable objects when passing `Stock` between activities

## 🧪 Test Tips

* Test network failure handling
* Ensure SDK analytics fire correctly
* Check if prices load based on time (e.g., 07:00 onward)
* Simulate edge cases: no results, no internet, etc.


## 🔗 Related Links

* [Android SDK Integration](sdk.md)
* [API Reference](api.md)
* [GitHub Repository](https://github.com/Ofir-Evgi/StockMarketAPI)
