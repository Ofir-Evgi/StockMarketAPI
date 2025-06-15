# �� StockMarketAPI SDK Documentation

## Overview

The StockMarketAPI SDK provides a comprehensive set of views and components for integrating stock market functionality into your Android application. This documentation covers all available views, their features, and implementation details.

## Views

### 1. Stock Selection View (`StockSelectionView`)

A searchable dropdown view for selecting stocks.

```java
StockMarketSDK.attachStockSelectionView(context, container);
```

**Features:**

- Real-time stock search
- Symbol and company name search
- Auto-complete suggestions
- Customizable appearance
- Event callbacks for selection

**Events:**

```java
StockMarketSDK.setOnStockSelectedListener((symbol, name) -> {
    // Handle stock selection
});
```

### 2. Stock List View (`StockListView`)

Displays a list of stocks with real-time updates.

```java
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachStockListView(context, container, symbols, 5000);
```

**Features:**

- Real-time price updates
- Price change indicators
- Customizable refresh interval
- Sortable columns
- Click handling

**Configuration:**

```java
StockListViewConfig config = new StockListViewConfig.Builder()
    .setRefreshInterval(5000)
    .setShowVolume(true)
    .setShowChange(true)
    .build();
```

### 3. Global Indices View (`GlobalIndexView`)

Shows major global market indices.

```java
StockMarketSDK.attachGlobalIndexView(context, container);
```

**Features:**

- Major indices display
- Real-time updates
- Change indicators
- Customizable appearance
- Click handling

**Indices Included:**

- S&P 500
- NASDAQ
- Dow Jones
- FTSE 100
- DAX
- NIKKEI 225

### 4. Stock Comparison View (`StockComparisonView`)

Compare multiple stocks side by side.

```java
List<String> symbols = Arrays.asList("AAPL", "GOOGL", "MSFT");
StockMarketSDK.attachStockComparisonView(context, container, symbols);
```

**Features:**

- Multiple stock comparison
- Price change comparison
- Volume comparison
- Customizable time range
- Interactive charts

### 5. Stock Graph View (`StockGraphView`)

Interactive chart for stock price visualization.

```java
StockMarketSDK.attachStockGraphView(context, container, "AAPL");
```

**Features:**

- Multiple timeframes
- Technical indicators
- Zoom and pan
- Price markers
- Volume display

**Timeframes:**

- 1D
- 1W
- 1M
- 3M
- 1Y
- 5Y

### 6. Stock Search View (`StockSearchView`)

Advanced stock search functionality.

```java
StockMarketSDK.attachStockSearchView(context, container);
```

**Features:**

- Real-time search
- Multiple search criteria
- Search history
- Favorites
- Recent searches

## Data Models

### Stock

```java
public class Stock {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePercent;
    private long volume;
    private long marketCap;
    private double peRatio;
    private double eps;
    private double dividend;
    private double dividendYield;
}
```

### MarketIndex

```java
public class MarketIndex {
    private String name;
    private double value;
    private double change;
    private double changePercent;
    private String currency;
}
```

## Error Handling

The SDK provides comprehensive error handling:

```java
StockMarketSDK.setOnErrorListener(error -> {
    switch (error.getType()) {
        case NETWORK_ERROR:
            // Handle network issues
            break;
        case INVALID_SYMBOL:
            // Handle invalid stock symbol
            break;
        case RATE_LIMIT:
            // Handle API rate limiting
            break;
    }
});
```

## Performance Optimization

### Memory Management

- Views are automatically detached when the activity is destroyed
- Background tasks are properly managed
- Resources are released when not needed

### Network Optimization

- Efficient API calls
- Caching where appropriate
- Batch updates for multiple stocks

## Customization

### Themes

```java
StockMarketSDK.setTheme(new StockMarketTheme(
    Color.BLUE,    // Primary color
    Color.GREEN,   // Positive change color
    Color.RED,     // Negative change color
    Color.GRAY     // Neutral color
));
```

### Layout

```java
StockMarketSDK.setLayout(new StockMarketLayout(
    true,   // Show volume
    true,   // Show change
    true,   // Show percentage
    false   // Show market cap
));
```

## Analytics

The SDK automatically tracks various events:

```java
StockMarketSDK.setOnAnalyticsListener(event -> {
    switch (event.getType()) {
        case VIEW_OPENED:
            // Track view opened
            break;
        case STOCK_SELECTED:
            // Track stock selection
            break;
        case SEARCH_PERFORMED:
            // Track search
            break;
    }
});
```

## Best Practices

1. **Lifecycle Management**

   - Always detach views in `onDestroy()`
   - Handle configuration changes
   - Manage background tasks

2. **Error Handling**

   - Implement error listeners
   - Provide user feedback
   - Handle network issues

3. **Performance**

   - Use appropriate refresh intervals
   - Implement caching
   - Monitor memory usage

4. **User Experience**
   - Provide loading indicators
   - Handle edge cases
   - Implement proper error messages

## Troubleshooting

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

### Debug Mode

Enable debug mode for detailed logging:

```java
StockMarketSDK.setDebugMode(true);
```

## Support

For additional support:

- Check the [example app](example-app.md)
- Review the [API documentation](api.md)
- Contact: [evgiofir1@gmail.com](mailto:evgiofir1@gmail.com)
