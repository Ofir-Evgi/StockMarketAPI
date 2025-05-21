package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            }

            @Override
            public void onFailure(Call<List<GlobalIndexData>> call, Throwable t) {
                Log.e("GlobalIndexView", "Failed to load data: " + t.getMessage());
            }
        });
    }

    private void updateUI(List<GlobalIndexData> indices) {
        indexContainer.removeAllViews();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        for (GlobalIndexData index : indices) {
            List<GlobalIndexPrice> prices = index.getPrices();
            if (prices == null || prices.isEmpty()) continue;

            GlobalIndexPrice priceToShow = findPriceForTime(prices, currentTime);

            String displayName = getDisplayName(index.getSymbol());
            String nameValue = displayName + ": " + priceToShow.getPrice();
            String percent = " (" + priceToShow.getPercent() + ")";

            SpannableString spannable = new SpannableString(nameValue + percent);

            int start = nameValue.length();
            int end = spannable.length();

            int colorRes = priceToShow.getPercent().contains("-") ?
                    android.R.color.holo_red_dark : android.R.color.holo_green_dark;

            spannable.setSpan(
                    new ForegroundColorSpan(getResources().getColor(colorRes, null)),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannable.setSpan(
                    new ForegroundColorSpan(getResources().getColor(android.R.color.black, null)),
                    0,
                    start,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            TextView tv = new TextView(getContext());
            tv.setText(spannable);
            tv.setTextSize(16f);
            tv.setPadding(0, 6, 0, 6);
            tv.setTypeface(null, Typeface.BOLD);

            indexContainer.addView(tv);
        }
    }

    private GlobalIndexPrice findPriceForTime(List<GlobalIndexPrice> prices, String timeNow) {
        if (prices == null || prices.isEmpty()) return prices.get(0);

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

    private String getDisplayName(String symbol) {
        switch (symbol) {
            case "^GSPC": return "S&P 500";
            case "^IXIC": return "NASDAQ";
            case "^DJI": return "Dow Jones";
            case "^GDAXI": return "DAX";
            case "^FTSE": return "FTSE 100";
            default: return symbol;
        }
    }
}
