package com.example.stockmarketsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.StockGraphActivity;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.services.OnStocksLoadedListener;
import com.example.stockmarketsdk.services.StockService;

import java.util.List;

public class StockSelectionView extends LinearLayout {

    private RecyclerView recyclerView;
    private StockSelectionAdapter adapter;

    public StockSelectionView(Context context) {
        super(context);
        init(context);
    }

    public StockSelectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_stock_selection, this, true);

        recyclerView = findViewById(R.id.recyclerStocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new StockSelectionAdapter(stock -> {
            AnalyticsTracker.logStockView(context, stock.getSymbol(), context.getPackageName());
            StockGraphActivity.start(context, stock);
        });

        recyclerView.setAdapter(adapter);

        StockService.getStocks(new OnStocksLoadedListener() {
            @Override
            public void onSuccess(List<Stock> stocks) {
                adapter.setStocks(stocks);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Error loading stocks: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
