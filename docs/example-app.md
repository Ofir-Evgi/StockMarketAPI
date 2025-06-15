# 📱 StockMarketAPI Example App Guide

This guide demonstrates how to implement a complete stock market application using the StockMarketAPI SDK.

## Project Structure

```
app/
├── java/
│   └── com.example.stockmarket/
│       ├── MainActivity.kt
│       ├── StockListActivity.kt
│       ├── StockDetailActivity.kt
│       ├── GlobalIndicesActivity.kt
│       └── ComparisonActivity.kt
└── res/
    └── layout/
        ├── activity_main.xml
        ├── activity_stock_list.xml
        ├── activity_stock_detail.xml
        ├── activity_global_indices.xml
        └── activity_comparison.xml
```

## Implementation

### 1. Main Activity

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SDK
        StockMarketSDK.initialize(this)

        // Set up navigation
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.stockListButton.setOnClickListener {
            startActivity(Intent(this, StockListActivity::class.java))
        }

        binding.globalIndicesButton.setOnClickListener {
            startActivity(Intent(this, GlobalIndicesActivity::class.java))
        }

        binding.comparisonButton.setOnClickListener {
            startActivity(Intent(this, ComparisonActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StockMarketSDK.detach()
    }
}
```

### 2. Stock List Activity

```kotlin
class StockListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_list)

        // Configure stock list
        val config = StockListViewConfig.Builder()
            .setRefreshInterval(5000)
            .setShowVolume(true)
            .setShowChange(true)
            .build()

        // Attach stock list view
        StockMarketSDK.attachStockListView(
            this,
            binding.stockListContainer,
            listOf("AAPL", "GOOGL", "MSFT", "AMZN"),
            config
        )

        // Handle stock selection
        StockMarketSDK.setOnStockSelectedListener { symbol, name ->
            val intent = Intent(this, StockDetailActivity::class.java).apply {
                putExtra("symbol", symbol)
                putExtra("name", name)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StockMarketSDK.detach()
    }
}
```

### 3. Stock Detail Activity

```kotlin
class StockDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_detail)

        val symbol = intent.getStringExtra("symbol") ?: return
        val name = intent.getStringExtra("name") ?: return

        // Set up toolbar
        supportActionBar?.apply {
            title = "$name ($symbol)"
            setDisplayHomeAsUpEnabled(true)
        }

        // Attach stock graph view
        StockMarketSDK.attachStockGraphView(
            this,
            binding.graphContainer,
            symbol
        )

        // Configure graph
        StockMarketSDK.setGraphConfig(
            GraphConfig.Builder()
                .setTimeframe(Timeframe.ONE_MONTH)
                .setShowVolume(true)
                .setShowMA(true)
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        StockMarketSDK.detach()
    }
}
```

### 4. Global Indices Activity

```kotlin
class GlobalIndicesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_indices)

        // Attach global indices view
        StockMarketSDK.attachGlobalIndexView(
            this,
            binding.indicesContainer
        )

        // Handle index selection
        StockMarketSDK.setOnIndexSelectedListener { index ->
            Toast.makeText(this, "Selected: ${index.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StockMarketSDK.detach()
    }
}
```

### 5. Comparison Activity

```kotlin
class ComparisonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparison)

        // Attach stock comparison view
        StockMarketSDK.attachStockComparisonView(
            this,
            binding.comparisonContainer,
            listOf("AAPL", "GOOGL", "MSFT")
        )

        // Configure comparison
        StockMarketSDK.setComparisonConfig(
            ComparisonConfig.Builder()
                .setTimeframe(Timeframe.ONE_MONTH)
                .setShowVolume(true)
                .setShowPercentage(true)
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        StockMarketSDK.detach()
    }
}
```

## Layout Files

### 1. Main Activity Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <Button
        android:id="@+id/stockListButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Stock List" />

    <Button
        android:id="@+id/globalIndicesButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Global Indices" />

    <Button
        android:id="@+id/comparisonButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Stock Comparison" />

</LinearLayout>
```

### 2. Stock List Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/stockListContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

## Features Implemented

1. **Stock List**

   - Real-time updates
   - Price changes
   - Volume display
   - Click handling

2. **Stock Detail**

   - Interactive graph
   - Multiple timeframes
   - Technical indicators
   - Volume display

3. **Global Indices**

   - Major indices
   - Real-time updates
   - Change indicators
   - Click handling

4. **Stock Comparison**
   - Multiple stocks
   - Parallel display
   - Price comparison
   - Volume comparison

## Best Practices

1. **Lifecycle Management**

   - Proper initialization
   - Resource cleanup
   - Memory management

2. **Error Handling**

   - Network errors
   - Invalid symbols
   - Rate limiting

3. **User Experience**

   - Loading indicators
   - Error messages
   - Smooth transitions

4. **Performance**
   - Efficient updates
   - Background tasks
   - Resource optimization

## Running the Example

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device/emulator

## Support

For additional help:

- Check the [SDK documentation](sdk.md)
- Review the [API documentation](api.md)
- Contact: [evgiofir1@gmail.com](mailto:evgiofir1@gmail.com)
