package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.StockPrice;

import java.util.List;

public class StockCardView extends LinearLayout {

    private TextView stockName;
    private TextView stockSymbol;
    private TextView stockPrice;
    private ImageView priceArrow;
    private MiniChartView miniChartView;

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

        stockName = findViewById(R.id.stockName);
        stockSymbol = findViewById(R.id.stockSymbol);
        stockPrice = findViewById(R.id.stockPrice);
        priceArrow = findViewById(R.id.priceArrow);
        miniChartView = findViewById(R.id.miniChart);
    }

    public void setStockData(String name, String symbol, Double price) {
        stockName.setText(name != null ? name : "N/A");
        stockSymbol.setText(symbol != null ? symbol : "N/A");

        if (price != null) {
            stockPrice.setText("$" + String.format("%.2f", price));
            stockPrice.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            stockPrice.setText("N/A");
            stockPrice.setTextColor(Color.GRAY);
            priceArrow.setVisibility(GONE);
        }
    }

    public void setHistory(List<StockPrice> history) {
        if (miniChartView != null) {
            miniChartView.setHistoryData(history);

            if (history != null && history.size() >= 2) {
                float first = (float) history.get(0).getPrice();
                float last = (float) history.get(history.size() - 1).getPrice();

                double changePercent = ((last - first) / first) * 100;
                String formattedPrice = String.format("$%.2f (%.2f%%)", last, changePercent);
                stockPrice.setText(formattedPrice);

                if (changePercent > 0) {
                    stockPrice.setTextColor(Color.parseColor("#4CAF50"));
                    priceArrow.setImageResource(R.drawable.ic_arrow_up);
                    priceArrow.setColorFilter(Color.parseColor("#4CAF50"));
                    priceArrow.setVisibility(VISIBLE);
                } else if (changePercent < 0) {
                    stockPrice.setTextColor(Color.parseColor("#F44336"));
                    priceArrow.setImageResource(R.drawable.ic_arrow_down);
                    priceArrow.setColorFilter(Color.parseColor("#F44336"));
                    priceArrow.setVisibility(VISIBLE);
                } else {
                    stockPrice.setTextColor(Color.DKGRAY);
                    priceArrow.setVisibility(GONE);
                }
            } else {
                priceArrow.setVisibility(GONE);
            }
        }
    }
}
