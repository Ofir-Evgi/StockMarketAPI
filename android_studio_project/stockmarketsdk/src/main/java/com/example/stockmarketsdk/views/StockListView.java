package com.example.stockmarketsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.AnalyticsTracker;

import java.util.ArrayList;
import java.util.List;

public class StockListView extends LinearLayout {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private StockListAdapter adapter;

    private StockListAdapter.OnStockClickListener clickListener;
    private List<Stock> allStocks = new ArrayList<>();

    private String userId;

    public StockListView(Context context) {
        super(context);
        init(context);
    }

    public StockListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_stock_list, this, true);
        recyclerView = findViewById(R.id.stockRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyText = findViewById(R.id.emptyText);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        userId = context.getPackageName();
    }

    public void setStocks(List<Stock> stocks) {
        allStocks = stocks != null ? stocks : new ArrayList<>();
        showLoading(false);

        if (allStocks.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new StockListAdapter(allStocks, stock -> {
                AnalyticsTracker.logStockView(getContext(), stock.getSymbol(), userId);
                if (clickListener != null) {
                    clickListener.onStockClick(stock);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    public void setOnStockClickListener(StockListAdapter.OnStockClickListener listener) {
        this.clickListener = listener;
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        emptyText.setVisibility(View.GONE);
    }

    public void filterByText(String query) {
        if (adapter == null || allStocks == null) return;

        List<Stock> filtered = new ArrayList<>();
        for (Stock stock : allStocks) {
            if (stock.getSymbol().toLowerCase().contains(query.toLowerCase()) ||
                    stock.getCompany_name().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(stock);
            }
        }

        adapter = new StockListAdapter(filtered, stock -> {
            AnalyticsTracker.logStockView(getContext(), stock.getSymbol(), userId);
            if (clickListener != null) {
                clickListener.onStockClick(stock);
            }
        });

        recyclerView.setAdapter(adapter);
        emptyText.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
