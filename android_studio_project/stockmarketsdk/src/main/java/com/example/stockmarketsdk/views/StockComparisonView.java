package com.example.stockmarketsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;

public class StockComparisonView extends LinearLayout {

    private LinearLayout comparisonContainer;

    public StockComparisonView(Context context) {
        super(context);
        init(context);
    }

    public StockComparisonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_stock_comparison, this, true);
        comparisonContainer = findViewById(R.id.comparisonContainer);
        setOrientation(VERTICAL);
    }

    public void addStock(Stock stock) {
        if (stock == null) return;

        StockCardView card = new StockCardView(getContext());
        card.setFullStockData(stock);

        comparisonContainer.addView(card);
    }


    public void clear() {
        comparisonContainer.removeAllViews();
    }
}
