package com.example.stockmarketsdk;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.services.OnStocksLoadedListener;
import com.example.stockmarketsdk.services.StockService;
import com.example.stockmarketsdk.views.GlobalIndexView;
import com.example.stockmarketsdk.views.StockComparisonScreenView;
import com.example.stockmarketsdk.views.StockListView;
import com.example.stockmarketsdk.views.StockSearchAndListView;
import com.example.stockmarketsdk.views.StockSelectionView;

import java.util.ArrayList;
import java.util.List;

public class StockMarketSDK {

    private static Handler handler;
    private static Runnable updateTask;
    private static List<String> symbols;
    private static int refreshIntervalMs = 5000;

    private static StockListView stockListView;
    private static GlobalIndexView globalIndexView;

    // --------------------- STOCK LIST ---------------------
    public static void attachStockListView(
            @NonNull Context context,
            @NonNull ViewGroup container,
            @NonNull List<String> stockSymbols,
            int refreshIntervalMillis
    ) {
        symbols = new ArrayList<>(stockSymbols);
        refreshIntervalMs = refreshIntervalMillis;
        handler = new Handler();

        stockListView = new StockListView(context);
        container.removeAllViews();
        container.addView(stockListView);

        loadAndDisplayStocks(context);

        updateTask = () -> {
            loadAndDisplayStocks(context);
            handler.postDelayed(updateTask, refreshIntervalMs);
        };
        handler.postDelayed(updateTask, refreshIntervalMs);

        stockListView.setOnStockClickListener(stock -> {
            StockGraphActivity.start(context, stock);
        });
    }

    private static void loadAndDisplayStocks(Context context) {
        StockService.getStocks(new OnStocksLoadedListener() {
            @Override
            public void onSuccess(List<Stock> stocks) {
                List<Stock> filtered = new ArrayList<>();
                for (Stock s : stocks) {
                    if (symbols.contains(s.getSymbol())) filtered.add(s);
                }
                stockListView.setStocks(filtered);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    // --------------------- GLOBAL INDEX ---------------------
    public static void attachGlobalIndexView(
            @NonNull Context context,
            @NonNull ViewGroup container
    ) {
        globalIndexView = new GlobalIndexView(context);
        container.removeAllViews();
        container.addView(globalIndexView);
        globalIndexView.loadData();
    }

    // --------------------- CLEANUP ---------------------
    public static void detach() {
        if (handler != null && updateTask != null) {
            handler.removeCallbacks(updateTask);
        }

        if (globalIndexView != null) {
            globalIndexView.stopRefreshing();
        }
    }

    public static void attachStockSelectionView(
            @NonNull Context context,
            @NonNull ViewGroup container
    ) {
        StockSelectionView selectionView = new StockSelectionView(context);
        container.removeAllViews();
        container.addView(selectionView);
    }


    public static void attachSearchableStockListView(
            @NonNull Context context,
            @NonNull ViewGroup container
    ) {
        refreshIntervalMs = 5000;
        handler = new Handler();

        StockSearchAndListView searchableView = new StockSearchAndListView(context);
        container.removeAllViews();
        container.addView(searchableView);

        stockListView = searchableView.findViewById(R.id.listView);

        loadAndDisplayStocksForSearchable(context, searchableView);

        updateTask = () -> {
            loadAndDisplayStocksForSearchable(context, searchableView);
            handler.postDelayed(updateTask, refreshIntervalMs);
        };
        handler.postDelayed(updateTask, refreshIntervalMs);

        searchableView.setOnStockClickListener(stock -> {
            StockGraphActivity.start(context, stock);
        });
    }



    public static void attachSearchableListView(
            @NonNull Context context,
            @NonNull ViewGroup container,
            @NonNull List<String> stockSymbols,
            int refreshIntervalMillis
    ) {
        symbols = new ArrayList<>(stockSymbols);
        refreshIntervalMs = refreshIntervalMillis;
        handler = new Handler();

        StockSearchAndListView searchableView = new StockSearchAndListView(context);
        container.removeAllViews();
        container.addView(searchableView);

        stockListView = searchableView.findViewById(R.id.listView);
        loadAndDisplayStocksForSearchable(context, searchableView);

        updateTask = () -> {
            loadAndDisplayStocksForSearchable(context, searchableView);
            handler.postDelayed(updateTask, refreshIntervalMs);
        };
        handler.postDelayed(updateTask, refreshIntervalMs);

        searchableView.setOnStockClickListener(stock -> {
            StockGraphActivity.start(context, stock);
        });
    }


    private static void loadAndDisplayStocksForSearchable(Context context, StockSearchAndListView searchableView) {
        StockService.getStocks(new OnStocksLoadedListener() {
            @Override
            public void onSuccess(List<Stock> stocks) {
                List<Stock> filtered = new ArrayList<>();
                for (Stock s : stocks) {
                    if (symbols.contains(s.getSymbol())) filtered.add(s);
                }
                searchableView.setStocks(filtered);
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    public static void attachStockComparisonView(
            @NonNull Context context,
            @NonNull ViewGroup container
    ) {
        StockComparisonScreenView comparisonView = new StockComparisonScreenView(context);
        container.removeAllViews();
        container.addView(comparisonView);
    }

}
