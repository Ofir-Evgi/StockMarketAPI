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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockHistory;
import com.example.stockmarketsdk.models.StockPrice;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StockPageView extends LinearLayout {

    private ImageView stockLogo, stockBg, backBtn;
    private TextView stockName, stockSymbol, stockDesc, stockPrice;
    private StockGraphView chart;
    private RecyclerView newsList;

    private Handler priceUpdateHandler = new Handler();
    private Runnable priceUpdateRunnable;

    private List<StockPrice> currentPrices;

    public StockPageView(Context context) {
        super(context);
        init(context);
    }

    public StockPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_stock_details, this, true);

        stockBg = findViewById(R.id.stockBg);
        stockLogo = findViewById(R.id.stockLogo);
        stockName = findViewById(R.id.stockName);
        stockSymbol = findViewById(R.id.stockSymbol);
        stockDesc = findViewById(R.id.stockDesc);
        stockPrice = findViewById(R.id.stockPrice);
        chart = findViewById(R.id.stockChart);
        newsList = findViewById(R.id.newsList);
        backBtn = findViewById(R.id.backBtn);

        newsList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> {
                Context ctx = getContext();
                if (ctx instanceof android.app.Activity) {
                    ((android.app.Activity) ctx).finish();
                }
            });
        }
    }

    public void setHistoryData(List<StockHistory> historyList) {
        if (historyList == null || historyList.isEmpty()) return;

        List<StockPrice> prices = new ArrayList<>();
        for (StockHistory history : historyList) {
            StockPrice price = new StockPrice();
            price.setTime(history.getDate());
            price.setPrice(history.getClose());
            prices.add(price);
        }

        chart.setStockPrices(prices);
    }

    public void setStock(Stock stock) {
        if (stock == null) return;

        stockName.setText(stock.getCompany_name());
        stockSymbol.setText("[" + stock.getSymbol() + "]");

        stockDesc.setText(stock.getDescription() != null && !stock.getDescription().isEmpty()
                ? stock.getDescription()
                : "No company description yet. Stay tuned.");

        if (stock.getNews() != null && !stock.getNews().isEmpty()) {
            NewsAdapter adapter = new NewsAdapter(stock.getNews());
            newsList.setAdapter(adapter);
        }

        currentPrices = stock.getPrices();
        chart.setStock(stock);
        updateLivePrice();

        priceUpdateRunnable = () -> {
            updateLivePrice();
            priceUpdateHandler.postDelayed(priceUpdateRunnable, 60 * 1000);
        };
        priceUpdateHandler.postDelayed(priceUpdateRunnable, 60 * 1000);

        loadImages(stock);
    }

    private void updateLivePrice() {
        if (currentPrices == null || currentPrices.isEmpty()) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String nowTime = LocalTime.now().format(formatter);

        double matchedPrice = -1;
        for (StockPrice sp : currentPrices) {
            if (sp.getTime().equals(nowTime)) {
                matchedPrice = sp.getPrice();
                break;
            }
        }

        if (matchedPrice != -1) {
            stockPrice.setText("$" + matchedPrice);
        } else {
            stockPrice.setText("$" + currentPrices.get(currentPrices.size() - 1).getPrice());
        }
    }

    private void loadImages(Stock stock) {
        if (stock.getLogoImageBase64() != null && !stock.getLogoImageBase64().isEmpty()) {
            Bitmap logoBitmap = decodeBase64Image(stock.getLogoImageBase64());
            if (logoBitmap != null) {
                stockLogo.setImageBitmap(logoBitmap);
            } else {
                stockLogo.setImageResource(getLogoResForSymbol(stock.getSymbol()));
            }
        } else {
            stockLogo.setImageResource(getLogoResForSymbol(stock.getSymbol()));
        }

        if (stock.getBgImageBase64() != null && !stock.getBgImageBase64().isEmpty()) {
            Bitmap bgBitmap = decodeBase64Image(stock.getBgImageBase64());
            if (bgBitmap != null) {
                stockBg.setImageBitmap(bgBitmap);
            } else {
                stockBg.setImageResource(getBgResForSymbol(stock.getSymbol()));
            }
        } else {
            stockBg.setImageResource(getBgResForSymbol(stock.getSymbol()));
        }
    }

    private Bitmap decodeBase64Image(String base64) {
        try {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            return null;
        }
    }

    private int getLogoResForSymbol(String symbol) {
        switch (symbol.toUpperCase()) {
            case "AAPL": return R.drawable.apple_logo;
            case "MSFT": return R.drawable.microsoft_logo;
            case "GOOGL": return R.drawable.google_logo;
            case "TSLA": return R.drawable.tesla_logo;
            case "META": return R.drawable.meta_logo;
            case "NVDA": return R.drawable.nvidia_logo;
            default: return R.drawable.stock_placeholder_logo;
        }
    }

    private int getBgResForSymbol(String symbol) {
        switch (symbol.toUpperCase()) {
            case "AAPL": return R.drawable.apple_bg;
            case "MSFT": return R.drawable.microsoft_bg;
            case "GOOGL": return R.drawable.google_bg;
            case "TSLA": return R.drawable.tesla_bg;
            case "META": return R.drawable.meta_bg;
            case "NVDA": return R.drawable.nvidia_bg;
            default: return R.drawable.stock_placeholder_bg;
        }
    }
}
