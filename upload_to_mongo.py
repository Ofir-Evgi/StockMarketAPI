import json
from pymongo import MongoClient

client = MongoClient("mongodb+srv://evgiofir1:Y5oJ5ka4PNyw16rg@stockcluster.egw5thu.mongodb.net/stockdb?retryWrites=true&w=majority&appName=StockCluster")
db = client["stockdb"]
stocks_collection = db["stocks"]
indices_collection = db["global_indices"]

with open("stock_prices.json", "r") as f:
    data = json.load(f)

stocks_collection.delete_many({})
indices_collection.delete_many({})

if "stocks" in data:
    stocks_collection.insert_many(data["stocks"])

if "Global Indices" in data:
    indices_collection.insert_many(data["Global Indices"])

print("Upload Succeed")
