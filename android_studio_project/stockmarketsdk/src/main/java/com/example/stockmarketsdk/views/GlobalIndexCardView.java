package com.example.stockmarketsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.GlobalIndexPrice;

public class GlobalIndexCardView extends LinearLayout {

    private TextView indexName, indexSymbol, indexValue, changeValue, changeAmount;
    private LinearLayout changeContainer;

    public GlobalIndexCardView(Context context) {
        super(context);
        init(context);
    }

    public GlobalIndexCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.market_index_item, this, true);
        setOrientation(HORIZONTAL);

        indexName = findViewById(R.id.indexName);
        indexSymbol = findViewById(R.id.indexSymbol);
        indexValue = findViewById(R.id.indexValue);
        changeValue = findViewById(R.id.changeValue);
        changeAmount = findViewById(R.id.changeAmount);
        changeContainer = (LinearLayout) changeValue.getParent();
    }

    public void setIndexData(String name, String symbol, GlobalIndexPrice price) {
        indexName.setText(name);
        indexSymbol.setText(symbol);
        indexValue.setText(String.format("%.2f", price.getPrice()));

        String percent = price.getPercent() != null ? price.getPercent() : "0.00%";
        boolean isNegative = percent.contains("-");

        double changeValueDouble = calculateChange(price.getPrice(), percent);
        String arrow = isNegative ? "▼" : "▲";
        String changeAmountText = arrow + " " + String.format("%.2f", Math.abs(changeValueDouble));

        changeValue.setText(percent);
        changeAmount.setText(changeAmountText);

        int bg = isNegative ? R.drawable.change_background_negative : R.drawable.change_background_positive;
        changeContainer.setBackgroundResource(bg);
    }

    private double calculateChange(double price, String percent) {
        try {
            String cleaned = percent.replace("%", "").replace("+", "").replace(",", "").trim();
            double perc = Double.parseDouble(cleaned);
            return price * perc / 100.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
