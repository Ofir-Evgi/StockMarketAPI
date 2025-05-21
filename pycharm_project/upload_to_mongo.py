import json
from pymongo import MongoClient

# חיבור למסד הנתונים
client = MongoClient("mongodb+srv://evgiofir1:Y5oJ5ka4PNyw16rg@stockcluster.egw5thu.mongodb.net/stockdb?retryWrites=true&w=majority&appName=StockCluster")
db = client["stockdb"]
stocks_collection = db["stocks"]
indices_collection = db["global_indices"]

# קריאה של קובץ ה-JSON
with open("stock_prices.json", "r") as f:
    data = json.load(f)

# ניקוי הקולקשנים (אופציונלי)
stocks_collection.delete_many({})
indices_collection.delete_many({})

# הוספת המניות
if "stocks" in data:
    stocks_collection.insert_many(data["stocks"])

# הוספת המדדים
if "Global Indices" in data:
    indices_collection.insert_many(data["Global Indices"])

print("✅ הנתונים הועלו בהצלחה לשני הקולקשנים במונגו!")
