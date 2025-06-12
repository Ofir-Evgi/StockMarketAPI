package com.example.stockmarketsdk.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.StockMarketApi;
import com.example.stockmarketsdk.models.GlobalIndexData;
import com.example.stockmarketsdk.models.GlobalIndexPrice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalIndexView extends LinearLayout {

    private LinearLayout indexContainer;
    private final Handler handler = new Handler();
    private final Runnable refreshRunnable = this::loadData;
    private static final long REFRESH_INTERVAL_MS = 60000; // כל דקה

    public GlobalIndexView(Context context) {
        super(context);
        init(context);
    }

    public GlobalIndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlobalIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_global_index, this, true);
        indexContainer = findViewById(R.id.indexContainer);
    }

    public void loadData() {
        StockMarketApi.getGlobalIndices().enqueue(new Callback<List<GlobalIndexData>>() {
            @Override
            public void onResponse(Call<List<GlobalIndexData>> call, Response<List<GlobalIndexData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Log.e("GlobalIndexView", "Empty or bad response");
                }
                scheduleNextRefresh();
            }

            @Override
            public void onFailure(Call<List<GlobalIndexData>> call, Throwable t) {
                Log.e("GlobalIndexView", "Failed to load data: " + t.getMessage());
                scheduleNextRefresh();
            }
        });
    }

    private void scheduleNextRefresh() {
        handler.removeCallbacks(refreshRunnable);
        handler.postDelayed(refreshRunnable, REFRESH_INTERVAL_MS);
    }

    public void stopRefreshing() {
        handler.removeCallbacks(refreshRunnable);
    }

    private void updateUI(List<GlobalIndexData> indices) {
        indexContainer.removeAllViews();
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        for (GlobalIndexData index : indices) {
            List<GlobalIndexPrice> prices = index.getPrices();
            if (prices == null || prices.isEmpty()) continue;

            GlobalIndexPrice priceToShow = findPriceForTime(prices, currentTime);
            if (priceToShow == null) continue;

            String name = index.getCompanyName() != null ? index.getCompanyName() : index.getSymbol();
            String symbol = index.getSymbol();

            GlobalIndexCardView cardView = new GlobalIndexCardView(getContext());
            cardView.setIndexData(name, symbol, priceToShow);

            indexContainer.addView(cardView);
        }
    }

    private GlobalIndexPrice findPriceForTime(List<GlobalIndexPrice> prices, String timeNow) {
        if (prices == null || prices.isEmpty()) return null;

        for (GlobalIndexPrice price : prices) {
            if (timeNow.equals(price.getTime())) {
                return price;
            }
        }

        GlobalIndexPrice fallback = null;
        for (GlobalIndexPrice price : prices) {
            if (price.getTime().compareTo(timeNow) <= 0) {
                fallback = price;
            } else {
                break;
            }
        }

        return fallback != null ? fallback : prices.get(prices.size() - 1);
    }
}
