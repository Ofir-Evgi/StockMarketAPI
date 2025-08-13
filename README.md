# 📈 StockMarketAPI SDK

*Live API*: [Render Deployment](https://stockmarketapi-qr65.onrender.com)  
*MongoDB Atlas*: Connected via MONGO_URI  
*Admin Dashboard*: Web portal for analytics & insights



# Application demo video




https://github.com/user-attachments/assets/e41f858d-8e06-44a0-9b66-740008d35efc





# Portal demo video




https://github.com/user-attachments/assets/5d0068b9-af1f-46fa-ba75-320c88deec95





## Overview

*StockMarketAPI* is a comprehensive mobile-first stock market SDK designed to simplify financial data integration into Android applications. The project includes:

- A RESTful backend service using Flask
- A public Android SDK (Java/Kotlin-compatible)
- A full-featured sample Android app
- A web-based admin analytics portal

---

## 🚀 Features

- 📊 Real-time stock data and global indices
- 📈 Interactive stock graph rendering and performance tracking
- 🔍 Advanced search and filtering capabilities
- 📱 SDK analytics tracking (screen views, time, clicks)
- ⚖ Multi-stock comparison functionality
- 🌐 Global market indices display
- 📋 Admin dashboard with interactive charts

---

## 📱 SDK Views & Screens

### 🏷 StockSelectionView

*Purpose*: A dedicated view for stock selection with a clean, focused interface.

*Key Features:*

- Simple, focused interface for stock selection
- Easy integration with minimal setup
- Automatic cleanup on detach

*Usage:*

java
public class StockSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout container = new FrameLayout(this);
        container.setId(View.generateViewId());
        setContentView(container);

        // Attach the view
        StockMarketSDK.attachStockSelectionView(this, container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StockMarketSDK.detach();
    }
}


### 🔍 StockSearchAndListView

*Purpose*: Combines search functionality with a list view for comprehensive stock browsing.

*Key Features:*

- Integrated search and list functionality
- Real-time stock updates
- Customizable refresh interval
- Automatic stock filtering
- Click handling for stock selection

*Usage:*

java
// Basic usage
StockMarketSDK.attachSearchableStockListView(context, container);

// Advanced usage with custom symbols and refresh interval
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachSearchableListView(
    context,
    container,
    symbols,
    5000  // Refresh every 5 seconds
);


### 📊 StockListView

*Purpose*: Displays a scrollable list of stocks with real-time updates.

*Key Features:*

- Real-time stock data updates
- Customizable refresh interval
- Symbol-based filtering
- Click handling for stock details
- Automatic cleanup

*Usage:*

java
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachStockListView(
    context,
    container,
    symbols,
    5000  // Refresh interval in milliseconds
);


### 🌍 GlobalIndexView

*Purpose*: Displays global market indices with real-time updates.

*Key Features:*

- Real-time index updates
- Clean, focused display
- Automatic refresh handling
- Proper cleanup on detach

*Usage:*

java
StockMarketSDK.attachGlobalIndexView(context, container);


### ⚖ StockComparisonScreenView

*Purpose*: Enables side-by-side comparison of multiple stocks.

*Key Features:*

- Multi-stock comparison
- Real-time data updates
- Interactive comparison interface
- Clean, organized layout

*Usage:*

java
StockMarketSDK.attachStockComparisonView(context, container);


### 📈 StockGraphActivity

*Purpose*: Displays a detailed stock graph for a single stock.

*Key Features:*

- Interactive graph rendering
- Zoom and pan options
- Stock price history
- Real-time updates
- Customized view

*Usage:*

java
// Opening the graph for a specific stock
StockGraphActivity.start(context, stock);


## 📱 SDK Views & Screens Documentation

### 1. 🏷 StockSelectionView

*Description*: A clean and simple stock selection screen.

*Key Features:*

- Minimalist and user-friendly interface
- Organized stock list display
- Single stock selection capability
- Click event support
- Automatic memory management

*Basic Usage:*

java
public class StockSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create container
        FrameLayout container = new FrameLayout(this);
        container.setId(View.generateViewId());
        setContentView(container);

        // Add the view
        StockMarketSDK.attachStockSelectionView(this, container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup resources
        StockMarketSDK.detach();
    }
}


*Customization:*

java
// Set specific stock symbols
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");

// Set refresh interval
int refreshInterval = 5000; // 5 seconds


### 2. 🔍 StockSearchAndListView

*Description*: Combined search and stock display screen with advanced search capabilities.

*Key Features:*

- Real-time search
- Automatic result filtering
- Dynamic list display
- Automatic data refresh
- Click event support

*Basic Usage:*

java
// Basic usage
StockMarketSDK.attachSearchableStockListView(context, container);

// Advanced usage
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachSearchableListView(
    context,
    container,
    symbols,
    5000  // Refresh interval in milliseconds
);


*Event Handling:*

java
searchableView.setOnStockClickListener(stock -> {
    // Handle stock selection
    StockGraphActivity.start(context, stock);
});


### 3. 📊 StockListView

*Description*: Stock list view with real-time updates.

*Key Features:*

- Organized list display
- Real-time price updates
- Symbol-based filtering
- Smooth scrolling
- Automatic memory management

*Basic Usage:*

java
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachStockListView(
    context,
    container,
    symbols,
    5000  // Refresh interval in milliseconds
);


*Error Handling:*

java
StockService.getStocks(new OnStocksLoadedListener() {
    @Override
    public void onSuccess(List<Stock> stocks) {
        // Handle success
    }

    @Override
    public void onError(String errorMessage) {
        // Handle error
    }
});


### 4. 🌍 GlobalIndexView

*Description*: Global market indices display with real-time updates.

*Key Features:*

- Global indices display
- Real-time price updates
- Clean and organized layout
- Automatic refresh
- Automatic memory management

*Basic Usage:*

java
StockMarketSDK.attachGlobalIndexView(context, container);


*Customization:*

java
// Set refresh interval
globalIndexView.setRefreshInterval(5000); // 5 seconds

// Handle click events
globalIndexView.setOnIndexClickListener(index -> {
    // Handle index click
});


### 5. ⚖ StockComparisonScreenView

*Description*: Screen for comparing multiple stocks with parallel display.

*Key Features:*

- Multiple stock comparison
- Interactive graph display
- Real-time price updates
- Add/remove stock capability
- Customizable display

*Basic Usage:*

java
StockMarketSDK.attachStockComparisonView(context, container);


*Customization:*

java
// Add stocks for comparison
comparisonView.addStock("AAPL");
comparisonView.addStock("GOOGL");

// Set refresh interval
comparisonView.setRefreshInterval(5000);

// Handle click events
comparisonView.setOnStockClickListener(stock -> {
    // Handle stock click
});


### 6. 📈 StockGraphActivity

*Description*: Detailed graph screen for individual stocks.

*Key Features:*

- Interactive graph display
- Zoom and pan capabilities
- Price history display
- Real-time updates
- Customizable display

*Basic Usage:*

java
// Open graph screen for specific stock
StockGraphActivity.start(context, stock);


*Customization:*

java
// Set time range
graphView.setTimeRange("1d"); // One day
graphView.setTimeRange("1w"); // One week
graphView.setTimeRange("1m"); // One month
graphView.setTimeRange("1y"); // One year

// Set chart style
graphView.setChartStyle(ChartStyle.LINE);
graphView.setChartStyle(ChartStyle.CANDLE);



### 7. 🔍 SearchableStockActivity

*Purpose*: A full-screen activity that provides comprehensive stock search functionality with card-based display.

*Key Features:*

- Card-based stock display with enhanced visual appeal
- Real-time search with instant filtering
- Interactive stock cards with detailed information
- Swipe gestures and touch interactions
- Advanced search algorithms with symbol and company name matching
- Customizable card layouts and themes
- Built-in loading states and error handling

*Usage:*

```java
// Basic usage - opens searchable activity
Intent intent = new Intent(context, SearchableStockActivity.class);
startActivity(intent);

// Advanced usage with custom configuration
Intent intent = new Intent(context, SearchableStockActivity.class);
intent.putExtra("SEARCH_MODE", "ADVANCED");
intent.putStringArrayListExtra("FILTER_SYMBOLS", symbols);
intent.putExtra("CARD_LAYOUT", "DETAILED");
startActivity(intent);
```

*Customization:*

```java
// Configure search parameters
SearchableStockActivity.Config config = new SearchableStockActivity.Config()
    .setSearchMode(SearchMode.REAL_TIME)
    .setCardStyle(CardStyle.MATERIAL_DESIGN)
    .setRefreshInterval(3000)
    .setMaxResults(50);

// Launch with custom config
SearchableStockActivity.startWithConfig(context, config);
```

*Event Handling:*

```java
// Handle stock card selection
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setOnStockCardClickListener(stock -> {
        // Open detailed view
        StockGraphActivity.start(this, stock);
    });
    
    setOnStockCardLongClickListener(stock -> {
        // Show context menu or quick actions
        showStockActions(stock);
        return true;
    });
}
```


## 🔧 System Requirements

### Minimum Requirements

- Android SDK 26 (Android 8.0)
- Java 11
- Internet permissions

### Gradle Settings

groovy
dependencies {
    implementation("com.github.Ofir-Evgi:StockMarketAPI:1.0.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
}


### Required Permissions

xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />


## 📊 Analytics

The library automatically tracks:

- Screen views
- Stock selections
- Search queries
- View duration
- User interactions

## 🔍 Troubleshooting

### Common Issues

1. *View Not Updating*

   - Check internet connection
   - Verify refresh interval
   - Check resource cleanup

2. *Memory Leaks*

   - Ensure detach() is called
   - Check lifecycle management
   - Verify handler cleanup

3. *Network Issues*
   - Check permissions
   - Verify endpoint
   - Check internet connection

---

## 📚 Additional Resources

- [Full Documentation](https://ofir-evgi.github.io/StockMarketAPI/)
- [API Reference](https://ofir-evgi.github.io/StockMarketAPI/api)
- [Sample App](https://github.com/Ofir-Evgi/StockMarketAPI/tree/main/app)

---

## 👨‍💻 Authors

*Ofir Evgi*  
*Omer Shukroon*

Advanced Mobile Seminar @ Afeka Academic College Of Engineering 2025

📧 Contact: [evgiofir1@gmail.com](mailto:evgiofir1@gmail.com)  
🔗 GitHub: [Ofir-Evgi](https://github.com/Ofir-Evgi)

---

## 📄 License

[MIT License](LICENSE)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software.
