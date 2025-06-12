package com.example.stockmarketsdk.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.AnalyticsTracker;

import java.util.ArrayList;
import java.util.List;

public class StockSearchView extends FrameLayout {

    private AutoCompleteTextView searchInput;
    private ProgressBar progressBar;
    private ArrayAdapter<String> suggestionsAdapter;
    private List<Stock> allStocks = new ArrayList<>();
    private List<Stock> currentResults = new ArrayList<>();
    private OnStockSelectedListener listener;

    private String userId;

    public interface OnStockSelectedListener {
        void onStockSelected(Stock stock);
        void onQueryTyped(String rawText);
    }

    public StockSearchView(Context context) {
        super(context);
        init(context);
    }

    public StockSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_stock_search, this, true);
        searchInput = findViewById(R.id.autoCompleteStock);
        progressBar = findViewById(R.id.searchProgress);

        userId = context.getPackageName();

        suggestionsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line);
        searchInput.setAdapter(suggestionsAdapter);
        searchInput.setThreshold(1);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                filterSuggestions(query);

                if (listener != null) {
                    listener.onQueryTyped(query);
                }
            }
        });

        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedText = suggestionsAdapter.getItem(position);
            for (Stock stock : currentResults) {
                String combined = stock.getCompany_name() + " (" + stock.getSymbol() + ")";
                if (combined.equals(selectedText)) {
                    AnalyticsTracker.logStockView(getContext(), stock.getSymbol(), userId);

                    if (listener != null) {
                        listener.onStockSelected(stock);
                    }
                    break;
                }
            }
        });
    }

    public void setOnStockSelectedListener(OnStockSelectedListener listener) {
        this.listener = listener;
    }

    public void setStockList(List<Stock> stocks) {
        this.allStocks = stocks != null ? stocks : new ArrayList<>();
    }

    private void filterSuggestions(String query) {
        currentResults.clear();
        suggestionsAdapter.clear();

        if (query.length() < 1) return;

        for (Stock stock : allStocks) {
            String nameSymbol = stock.getCompany_name() + " (" + stock.getSymbol() + ")";
            if (nameSymbol.toLowerCase().contains(query.toLowerCase())) {
                currentResults.add(stock);
                suggestionsAdapter.add(nameSymbol);
            }
        }

        suggestionsAdapter.notifyDataSetChanged();
    }
}
