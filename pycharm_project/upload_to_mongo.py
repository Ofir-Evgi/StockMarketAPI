import json
import base64
import os
from pymongo import MongoClient

client = MongoClient("mongodb+srv://evgiofir1:Y5oJ5ka4PNyw16rg@stockcluster.egw5thu.mongodb.net/stockdb?retryWrites=true&w=majority&appName=StockCluster")
db = client["stockdb"]
stocks_collection = db["stocks"]
indices_collection = db["global_indices"]

with open("stock_prices.json", "r", encoding="utf-8") as f:
    data = json.load(f)

image_dir = "images"

def encode_image(image_path):
    try:
        with open(image_path, "rb") as img:
            return base64.b64encode(img.read()).decode("utf-8")
    except Exception as e:
        print(f"⚠️ Error loading the image. {image_path}: {e}")
        return None

symbol_to_name = {
    "aapl": "apple",
    "msft": "microsoft",
    "googl": "google",
    "meta": "meta",
    "tsla": "tesla",
    "nvda": "nvidia"
}

stocks_collection.delete_many({})
indices_collection.delete_many({})

if "stocks" in data:
    for stock in data["stocks"]:
        symbol = stock.get("symbol", "").lower()
        if not symbol:
            continue

        name = symbol_to_name.get(symbol, "stock_placeholder")

        logo_path = os.path.join(image_dir, f"{name}_logo.png")
        bg_path = os.path.join(image_dir, f"{name}_bg.png")

        logo_encoded = encode_image(logo_path)
        bg_encoded = encode_image(bg_path)

        if logo_encoded:
            stock["logo_image_base64"] = logo_encoded
        if bg_encoded:
            stock["bg_image_base64"] = bg_encoded

    stocks_collection.insert_many(data["stocks"])

if "Global Indices" in data:
    indices_collection.insert_many(data["Global Indices"])

print("The data was successfully uploaded to MongoDB. ✅")
