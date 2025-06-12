package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.services.OnStocksLoadedListener;
import com.example.stockmarketsdk.services.StockService;

import java.util.ArrayList;
import java.util.List;

public class StockComparisonScreenView extends LinearLayout {

    private RecyclerView recyclerView;
    private Button compareButton;
    private LinearLayout chartContainer;

    private final List<Stock> allStocks = new ArrayList<>();
    private final List<Stock> selectedStocks = new ArrayList<>();
    private StockMultiSelectionAdapter adapter;

    public StockComparisonScreenView(Context context) {
        super(context);
        init(context);
    }

    public StockComparisonScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_stock_comparison_screen, this, true);
        recyclerView = findViewById(R.id.recyclerStocksMultiSelect);
        compareButton = findViewById(R.id.btnCompareStocks);
        chartContainer = findViewById(R.id.comparisonChartsContainer);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StockMultiSelectionAdapter(selected -> {
            selectedStocks.clear();
            selectedStocks.addAll(selected);
        });

        recyclerView.setAdapter(adapter);

        compareButton.setOnClickListener(v -> showOnlyCharts());

        loadStocks();
    }

    private void loadStocks() {
        StockService.getStocks(new OnStocksLoadedListener() {
            @Override
            public void onSuccess(List<Stock> stocks) {
                Log.d("StockComparisonScreen", "Loaded " + stocks.size() + " stocks");
                allStocks.clear();
                allStocks.addAll(stocks);
                adapter.setStocks(stocks);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("StockComparisonScreen", "Failed to load stocks: " + errorMessage);
                Toast.makeText(getContext(), "Error loading stocks: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOnlyCharts() {
        chartContainer.removeAllViews();

        if (selectedStocks.size() < 2) {
            Toast.makeText(getContext(), "Select at least two stocks for comparison.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Stock stock : selectedStocks) {
            AnalyticsTracker.logEvent(getContext(), "stock_compare", stock.getSymbol(), getContext().getPackageName());
        }


        for (Stock stock : selectedStocks) {
            Log.d("StockComparison", "Rendering graph for: " + stock.getSymbol());

            TextView stockTitle = new TextView(getContext());
            stockTitle.setText(stock.getCompany_name() + " (" + stock.getSymbol() + ")");
            stockTitle.setTextSize(16);
            stockTitle.setTypeface(null, Typeface.BOLD);
            stockTitle.setPadding(0, 24, 0, 8);
            chartContainer.addView(stockTitle);

            StockGraphView graphView = new StockGraphView(getContext());
            LinearLayout.LayoutParams graphParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    400
            );
            graphParams.setMargins(0, 0, 0, 24);
            graphView.setLayoutParams(graphParams);
            graphView.setStock(stock);

            chartContainer.addView(graphView);
        }
    }


}
