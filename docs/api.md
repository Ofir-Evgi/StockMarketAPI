# 🌐 StockMarketAPI - Backend API Guide

The backend is a **Flask-based RESTful API** that provides stock market data, global indices, and analytics. It uses **MongoDB Atlas** as the database and is deployed on **Render**.

---

## 📦 Deployment Details

- **Framework**: Flask + PyMongo
- **Cloud Hosting**: Render
- **Database**: MongoDB Atlas
- **API Base URL**: [`https://stockmarketapi-qr65.onrender.com`](https://stockmarketapi-qr65.onrender.com)
- **Authentication**: None (public API)

---

## 📘 Available Endpoints

### 📊 Stock Data

| Method | Endpoint                   | Description                    |
|--------|----------------------------|--------------------------------|
| GET    | `/stocks`                  | Get all stock entries          |
| GET    | `/stocks/<symbol>`         | Get a single stock by symbol   |
| POST   | `/stocks`                  | Add a new stock                |
| PUT    | `/stocks/<symbol>`         | Update existing stock data     |
| DELETE | `/stocks/<symbol>`         | Delete a stock                 |

### 🌍 Global Indices

| Method | Endpoint               | Description                   |
|--------|------------------------|-------------------------------|
| GET    | `/global-indices`      | Returns global market indices |

### 📈 Analytics

| Method | Endpoint                   | Description                         |
|--------|----------------------------|-------------------------------------|
| POST   | `/analytics/events`        | Log screen/stock interaction        |
| GET    | `/analytics/stock-clicks`  | Most viewed stocks                  |
| GET    | `/analytics/screen-views`  | Screen views count                  |
| GET    | `/analytics/top-duration`  | Average time spent per screen       |

---

## 🧪 Example Usage

### ✅ Get All Stocks

```bash
curl https://stockmarketapi-qr65.onrender.com/stocks
```

### ✅ Log Analytics Event

```bash
curl -X POST https://stockmarketapi-qr65.onrender.com/analytics/events \
  -H "Content-Type: application/json" \
  -d '{"event":"stock_view","symbol":"AAPL","user_id":"com.my.app"}'
```

## 📂 Backend File Structure

```
backend/
├── app.py              # Main Flask server
├── upload_to_mongo.py  # MongoDB seeding script
├── render.yaml         # Render deployment config
├── requirements.txt    # Python dependencies
```

## 🌐 Environment Variables

Make sure this is set on Render or locally:

```env
MONGO_URI=mongodb+srv://your-user:your-password@cluster.mongodb.net/stockdb
```

## 🧠 Notes

- The API returns data in JSON format
- Flask automatically handles CORS (via flask-cors)
- Real-time analytics events are timestamped in UTC
- Stocks and global indices are preloaded from stock_prices.json

## 📌 Need help connecting to MongoDB Atlas?

Check [Check official docs](https://www.mongodb.com/docs/atlas/getting-started/) for setup instructions.
