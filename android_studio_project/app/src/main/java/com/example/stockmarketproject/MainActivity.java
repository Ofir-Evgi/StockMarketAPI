package com.example.stockmarketproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.services.OnStocksLoadedListener;
import com.example.stockmarketsdk.services.StockService;
import com.example.stockmarketsdk.views.StockListView;
import com.example.stockmarketsdk.views.StockSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StockSearchView stockSearchView;
    private StockListView stockListView;
    private Button btnShowGraph, btnCompare, btnGoToIndices;

    private final ArrayList<Stock> selectedStocks = new ArrayList<>();
    private Stock lastSelected = null;
    private long startTimeMillis;

    private final ActivityResultLauncher<Intent> comparisonLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                selectedStocks.clear();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimeMillis = System.currentTimeMillis();
        String userId = getPackageName();
        AnalyticsTracker.logScreenView(this, "main_screen", userId);

        stockSearchView = findViewById(R.id.stockSearchView);
        stockListView = findViewById(R.id.stockListView);
        btnShowGraph = findViewById(R.id.btnShowGraph);
        btnCompare = findViewById(R.id.btnCompare);
        btnGoToIndices = findViewById(R.id.btnGoToIndices);


        StockService.getStocks(new OnStocksLoadedListener() {
            @Override
            public void onSuccess(List<Stock> stocks) {
                stockListView.setStocks(stocks);
                stockSearchView.setStockList(stocks);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error loading stocks: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        stockSearchView.setOnStockSelectedListener(new StockSearchView.OnStockSelectedListener() {
            @Override
            public void onStockSelected(Stock stock) {
                handleStockSelection(stock);
            }

            @Override
            public void onQueryTyped(String rawText) {
                stockListView.filterByText(rawText);
            }
        });

        stockListView.setOnStockClickListener(this::handleStockSelection);

        btnShowGraph.setOnClickListener(v -> {
            if (lastSelected != null) {
                Intent intent = new Intent(this, StockGraphActivity.class);
                intent.putExtra("stock", lastSelected);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Select a stock to view a chart", Toast.LENGTH_SHORT).show();
            }
        });

        btnCompare.setOnClickListener(v -> {
            if (selectedStocks.size() >= 2) {
                Intent intent = new Intent(this, StockComparisonActivity.class);
                intent.putExtra("stocks", selectedStocks);
                comparisonLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Select at least two stocks to compare", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToIndices.setOnClickListener(v -> {
            Intent intent = new Intent(this, GlobalIndicesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long durationSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        String userId = getPackageName();
        AnalyticsTracker.logScreenTime(this, "main_screen", durationSeconds, userId);
    }

    private void handleStockSelection(Stock stock) {
        lastSelected = stock;
        if (!selectedStocks.contains(stock)) {
            selectedStocks.add(stock);
        }

        Toast.makeText(this, "Stock Selected: " + stock.getSymbol(), Toast.LENGTH_SHORT).show();

        String userId = getPackageName();
        AnalyticsTracker.logStockView(this, stock.getSymbol(), userId);
    }
}
