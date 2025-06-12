package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;
import com.example.stockmarketsdk.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockCardView extends LinearLayout {

    private TextView stockName, stockSymbol, stockPrice, priceChange, priceChangePercent;
    private ImageView priceArrow, stockLogo;
    private MiniChartView miniChartView;
    private LinearLayout cardRootLayout;
    private boolean isRefreshing = false;
    private Handler handler = new Handler();
    private Runnable priceUpdater;
    private String currentSymbol;

    public StockCardView(Context context) {
        super(context);
        init(context);
    }

    public StockCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_stock_card, this, true);
        setOrientation(VERTICAL);

        cardRootLayout = findViewById(R.id.cardRoot);
        stockLogo = findViewById(R.id.stockLogo);
        stockName = findViewById(R.id.stockName);
        stockSymbol = findViewById(R.id.stockSymbol);
        stockPrice = findViewById(R.id.stockPrice);
        priceArrow = findViewById(R.id.priceArrow);
        priceChange = findViewById(R.id.priceChange);
        priceChangePercent = findViewById(R.id.priceChangePercent);
        miniChartView = findViewById(R.id.miniChart);
    }

    public void setFullStockData(Stock stock) {
        if (stock == null) return;

        currentSymbol = stock.getSymbol();

        stockName.setText(stock.getCompany_name() != null ? stock.getCompany_name() : "N/A");
        stockSymbol.setText(currentSymbol != null ? currentSymbol : "N/A");

        List<StockPrice> history = stock.getPrices();
        if (history != null && !history.isEmpty()) {
            double timeBasedPrice = getPriceByCurrentTime(history);
            stockPrice.setText("$" + String.format("%.2f", timeBasedPrice));
            stockPrice.setTextColor(Color.parseColor("#4CAF50"));
            setHistory(history);
        } else {
            stockPrice.setText("N/A");
            stockPrice.setTextColor(Color.GRAY);
            priceArrow.setVisibility(GONE);
        }

        String logoBase64 = stock.getLogoImageBase64();
        if (logoBase64 != null && !logoBase64.isEmpty()) {
            try {
                byte[] decoded = Base64.decode(logoBase64, Base64.DEFAULT);
                Bitmap logoBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                stockLogo.setImageBitmap(logoBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            stockLogo.setImageResource(R.drawable.stock_placeholder_logo);
        }

        startAutoRefresh();
    }



    private void startAutoRefresh() {
        if (isRefreshing) return;
        isRefreshing = true;

        priceUpdater = new Runnable() {
            @Override
            public void run() {
                fetchStockDataFromApi(currentSymbol);
                handler.postDelayed(this, 60000);
            }
        };

        handler.post(priceUpdater);
    }

    private void fetchStockDataFromApi(String symbol) {
        if (symbol == null) return;

        String url = "https://stockmarketapi-qr65.onrender.com/api/stock/" + symbol;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String name = response.optString("company_name", symbol);
                        JSONArray pricesJson = response.getJSONArray("prices");
                        String logoBase64 = response.optString("logo_image_base64", null);

                        List<StockPrice> history = new ArrayList<>();
                        for (int i = 0; i < pricesJson.length(); i++) {
                            JSONObject obj = pricesJson.getJSONObject(i);
                            StockPrice sp = new StockPrice();
                            sp.setTime(obj.getString("time"));
                            sp.setPrice(obj.getDouble("price"));
                            history.add(sp);
                        }

                        double lastPrice = history.get(history.size() - 1).getPrice();

                        stockName.setText(name);
                        stockPrice.setText("$" + String.format("%.2f", lastPrice));
                        setHistory(history);

                        if (logoBase64 != null && !logoBase64.isEmpty()) {
                            byte[] decoded = Base64.decode(logoBase64, Base64.DEFAULT);
                            Bitmap logoBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            stockLogo.setImageBitmap(logoBitmap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private double getPriceByCurrentTime(List<StockPrice> prices) {
        if (prices == null || prices.isEmpty()) return 0.0;

        String currentTime = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());

        for (StockPrice price : prices) {
            if (currentTime.equals(price.getTime())) {
                return price.getPrice();
            }
        }

        StockPrice fallback = null;
        for (StockPrice price : prices) {
            if (price.getTime().compareTo(currentTime) <= 0) {
                fallback = price;
            } else {
                break;
            }
        }

        return fallback != null ? fallback.getPrice() : prices.get(0).getPrice();
    }





    private void setHistory(List<StockPrice> history) {
        if (miniChartView != null) {
            miniChartView.setHistoryData(history);

            if (history.size() >= 2) {
                float first = (float) history.get(0).getPrice();
                float last = (float) history.get(history.size() - 1).getPrice();

                double change = last - first;
                double changePercentVal = (first != 0) ? ((last - first) / first) * 100 : 0;

                priceChange.setText(String.format("%+.2f", change));
                priceChangePercent.setText(String.format("(%+.2f%%)", changePercentVal));

                if (change > 0) {
                    priceChange.setTextColor(Color.parseColor("#34D399"));
                    priceArrow.setImageResource(R.drawable.ic_arrow_up);
                    priceArrow.setColorFilter(Color.parseColor("#34D399"));
                    priceArrow.setVisibility(VISIBLE);
                } else if (change < 0) {
                    priceChange.setTextColor(Color.parseColor("#EF4444"));
                    priceArrow.setImageResource(R.drawable.ic_arrow_down);
                    priceArrow.setColorFilter(Color.parseColor("#EF4444"));
                    priceArrow.setVisibility(VISIBLE);
                } else {
                    priceChange.setTextColor(Color.DKGRAY);
                    priceArrow.setVisibility(GONE);
                }
            } else {
                priceArrow.setVisibility(GONE);
                priceChange.setText("");
                priceChangePercent.setText("");
            }
        }
    }
}
