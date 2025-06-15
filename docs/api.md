# 🔌 StockMarketAPI Backend Documentation

## Overview

The StockMarketAPI backend provides a RESTful API for accessing real-time and historical stock market data. This documentation covers all available endpoints, request/response formats, and authentication requirements.

## Base URL

```
https://api.stockmarketapi.com/v1
```

## Authentication

All API requests require an API key. Include it in the request header:

```
Authorization: Bearer YOUR_API_KEY
```

## Rate Limits

- 100 requests per minute
- 1000 requests per hour
- 10000 requests per day

## Endpoints

### 1. Stock Data

#### Get Stock Quote

```http
GET /stocks/{symbol}
```

**Parameters:**

- `symbol` (required): Stock symbol (e.g., AAPL)

**Response:**

```json
{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "price": 150.25,
  "change": 2.5,
  "changePercent": 1.69,
  "volume": 50000000,
  "marketCap": 2500000000000,
  "peRatio": 25.5,
  "eps": 5.89,
  "dividend": 0.88,
  "dividendYield": 0.59,
  "timestamp": "2024-03-20T15:30:00Z"
}
```

#### Get Historical Data

```http
GET /stocks/{symbol}/history
```

**Parameters:**

- `symbol` (required): Stock symbol
- `interval` (optional): Data interval (1m, 5m, 15m, 1h, 1d)
- `range` (optional): Time range (1d, 5d, 1mo, 3mo, 6mo, 1y, 5y, max)

**Response:**

```json
{
  "symbol": "AAPL",
  "interval": "1d",
  "range": "1mo",
  "data": [
    {
      "timestamp": "2024-03-20T15:30:00Z",
      "open": 148.5,
      "high": 151.0,
      "low": 148.0,
      "close": 150.25,
      "volume": 50000000
    }
  ]
}
```

### 2. Market Indices

#### Get Global Indices

```http
GET /indices
```

**Response:**

```json
{
  "indices": [
    {
      "name": "S&P 500",
      "symbol": "^GSPC",
      "value": 4500.25,
      "change": 25.5,
      "changePercent": 0.57,
      "currency": "USD"
    }
  ]
}
```

### 3. Search

#### Search Stocks

```http
GET /search
```

**Parameters:**

- `query` (required): Search query
- `limit` (optional): Maximum results (default: 10)

**Response:**

```json
{
  "results": [
    {
      "symbol": "AAPL",
      "name": "Apple Inc.",
      "exchange": "NASDAQ",
      "type": "Common Stock"
    }
  ]
}
```

### 4. Company Info

#### Get Company Profile

```http
GET /stocks/{symbol}/profile
```

**Response:**

```json
{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "description": "Apple Inc. designs, manufactures, and markets smartphones...",
  "sector": "Technology",
  "industry": "Consumer Electronics",
  "website": "https://www.apple.com",
  "ceo": "Tim Cook",
  "employees": 154000,
  "headquarters": "Cupertino, California"
}
```

## Error Handling

### Error Response Format

```json
{
  "error": {
    "code": "INVALID_SYMBOL",
    "message": "Invalid stock symbol provided",
    "details": "The symbol 'INVALID' does not exist"
  }
}
```

### Common Error Codes

| Code              | Description                |
| ----------------- | -------------------------- |
| `INVALID_SYMBOL`  | Invalid stock symbol       |
| `RATE_LIMIT`      | Rate limit exceeded        |
| `UNAUTHORIZED`    | Invalid API key            |
| `SERVER_ERROR`    | Internal server error      |
| `INVALID_REQUEST` | Invalid request parameters |

## WebSocket API

### Connection

```
wss://api.stockmarketapi.com/v1/ws
```

### Authentication

```json
{
  "type": "auth",
  "apiKey": "YOUR_API_KEY"
}
```

### Subscribe to Stock Updates

```json
{
  "type": "subscribe",
  "channel": "stock",
  "symbol": "AAPL"
}
```

### Stock Update Format

```json
{
  "type": "stock_update",
  "symbol": "AAPL",
  "price": 150.25,
  "change": 2.5,
  "changePercent": 1.69,
  "volume": 50000000,
  "timestamp": "2024-03-20T15:30:00Z"
}
```

## Data Types

### Stock

```typescript
interface Stock {
  symbol: string;
  name: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
  marketCap: number;
  peRatio: number;
  eps: number;
  dividend: number;
  dividendYield: number;
  timestamp: string;
}
```

### MarketIndex

```typescript
interface MarketIndex {
  name: string;
  symbol: string;
  value: number;
  change: number;
  changePercent: number;
  currency: string;
}
```

### HistoricalData

```typescript
interface HistoricalData {
  timestamp: string;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
}
```

## Best Practices

1. **Error Handling**

   - Implement exponential backoff
   - Handle rate limits
   - Validate responses

2. **Performance**

   - Use WebSocket for real-time data
   - Implement caching
   - Batch requests when possible

3. **Security**
   - Keep API key secure
   - Use HTTPS
   - Validate input

## Support

For additional support:

- API Status: [status.stockmarketapi.com](https://status.stockmarketapi.com)
- Documentation: [docs.stockmarketapi.com](https://docs.stockmarketapi.com)
- Contact: [api@stockmarketapi.com](mailto:api@stockmarketapi.com)
