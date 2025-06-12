package com.example.stockmarketsdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockSearchAndListView extends LinearLayout {

    private StockSearchView searchView;
    private StockListView listView;

    private List<Stock> allStocks = new ArrayList<>();

    public StockSearchAndListView(Context context) {
        super(context);
        init(context);
    }

    public StockSearchAndListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_search_and_list, this, true);

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);

        searchView.setOnStockSelectedListener(new StockSearchView.OnStockSelectedListener() {
            @Override
            public void onStockSelected(Stock stock) {
            }

            @Override
            public void onQueryTyped(String rawText) {
                listView.filterByText(rawText);
            }
        });
    }

    public void setStocks(List<Stock> stocks) {
        if (stocks == null) stocks = new ArrayList<>();
        this.allStocks = stocks;

        searchView.setStockList(stocks);
        listView.setStocks(stocks);
    }

    public void setOnStockClickListener(StockListAdapter.OnStockClickListener listener) {
        listView.setOnStockClickListener(listener);
    }

    public void refreshStockPrices(List<Stock> updatedStocks) {
        setStocks(updatedStocks);
    }
}
