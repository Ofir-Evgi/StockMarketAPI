from flask import Flask, request, jsonify
from flask_cors import CORS
from pymongo import MongoClient
from datetime import datetime
import os

app = Flask(__name__)
CORS(app)

MONGO_URI = os.environ.get("MONGO_URI")
client = MongoClient(MONGO_URI)
db = client["stockdb"]
stocks_collection = db["stocks"]
analytics_collection = db["analytics"]
global_indices_collection = db["global_indices"]

@app.route("/")
def home():
    return "Stock Market API is running!"


@app.route("/stocks", methods=["GET"])
def get_stocks():
    stocks = list(stocks_collection.find({}, {"_id": 0}))
    return jsonify(stocks)

@app.route("/healthz")
def health_check():
    return "OK"

@app.route("/stocks/<symbol>", methods=["GET"])
def get_stock(symbol):
    stock = stocks_collection.find_one({"symbol": symbol.upper()}, {"_id": 0})
    if stock:
        return jsonify(stock)
    return jsonify({"error": "Stock not found"}), 404

@app.route("/stocks", methods=["POST"])
def add_stock():
    data = request.json
    if not data or "symbol" not in data:
        return jsonify({"error": "Missing stock data"}), 400

    existing = stocks_collection.find_one({"symbol": data["symbol"].upper()})
    if existing:
        return jsonify({"error": "Stock already exists"}), 400

    data["symbol"] = data["symbol"].upper()
    stocks_collection.insert_one(data)
    return jsonify({"message": "Stock added successfully!"})

@app.route("/stocks/<symbol>", methods=["PUT"])
def update_stock(symbol):
    data = request.json
    if not data:
        return jsonify({"error": "Missing update data"}), 400

    result = stocks_collection.update_one(
        {"symbol": symbol.upper()},
        {"$set": data}
    )

    if result.matched_count == 0:
        return jsonify({"error": "Stock not found"}), 404

    return jsonify({"message": "Stock updated successfully"})

@app.route("/stocks/<symbol>", methods=["DELETE"])
def delete_stock(symbol):
    result = stocks_collection.delete_one({"symbol": symbol.upper()})
    if result.deleted_count == 0:
        return jsonify({"error": "Stock not found"}), 404

    return jsonify({"message": "Stock deleted successfully"})


@app.route("/analytics/events", methods=["POST"])
def log_event():
    data = request.json
    if not data:
        return jsonify({"error": "Missing event data"}), 400

    data["timestamp"] = datetime.utcnow()
    analytics_collection.insert_one(data)
    return jsonify({"message": "Event logged"})

@app.route("/analytics/stock-clicks", methods=["GET"])
def stock_clicks():
    result = analytics_collection.aggregate([
        {"$match": {"event": "stock_view"}},
        {"$group": {"_id": "$symbol", "clicks": {"$sum": 1}}},
        {"$sort": {"clicks": -1}}
    ])
    return jsonify(list(result))

@app.route("/analytics/top-stock", methods=["GET"])
def top_stock():
    result = analytics_collection.aggregate([
        {"$match": {"event": "stock_view"}},
        {"$group": {"_id": "$symbol", "views": {"$sum": 1}}},
        {"$sort": {"views": -1}},
        {"$limit": 1}
    ])
    return jsonify(list(result))

@app.route("/analytics/screen-views", methods=["GET"])
def screen_views():
    result = analytics_collection.aggregate([
        {"$match": {"event": "screen_view"}},
        {"$group": {"_id": "$screen", "views": {"$sum": 1}}},
        {"$sort": {"views": -1}}
    ])
    return jsonify(list(result))

@app.route("/analytics/top-duration", methods=["GET"])
def top_duration():
    result = analytics_collection.aggregate([
        {"$match": {"event": "screen_time"}},
        {"$group": {"_id": "$screen", "avg_time": {"$avg": "$duration"}}},
        {"$sort": {"avg_time": -1}}
    ])
    return jsonify(list(result))


@app.route("/analytics/stock-comparisons", methods=["GET"])
def stock_comparisons():
    result = analytics_collection.aggregate([
        {"$match": {"event": "stock_compare"}},
        {"$group": {"_id": "$symbol", "comparisons": {"$sum": 1}}},
        {"$sort": {"comparisons": -1}}
    ])
    return jsonify(list(result))


@app.route("/global-indices", methods=["GET"])
def get_global_indices():
    indices = list(db["global_indices"].find({}, {"_id": 0}))
    return jsonify(indices)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)
