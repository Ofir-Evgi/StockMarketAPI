package com.example.stockmarketproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.StockMarketApi;
import com.example.stockmarketsdk.models.BaseResponse;
import com.example.stockmarketsdk.models.StockHistory;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.views.StockPageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDetailActivity extends AppCompatActivity {

    private StockPageView stockPageView;
    private TextView stockNameTextView;
    private String symbol;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        stockPageView = findViewById(R.id.stockGraphView);
        stockNameTextView = findViewById(R.id.stockNameTextView);

        symbol = getIntent().getStringExtra("symbol");
        name = getIntent().getStringExtra("name");

        stockNameTextView.setText(name + " (" + symbol + ")");

        String userId = getPackageName();

        AnalyticsTracker.logStockView(this, symbol, userId);

        AnalyticsTracker.logScreenView(this, "stock_detail_" + symbol, userId);

        loadStockHistory();
    }

    private void loadStockHistory() {
        StockMarketApi.getStockHistory(symbol, "1y").enqueue(new Callback<BaseResponse<List<StockHistory>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<StockHistory>>> call, Response<BaseResponse<List<StockHistory>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<StockHistory> historyList = response.body().getData();
                    if (!historyList.isEmpty()) {
                        stockPageView.setHistoryData(historyList);
                    } else {
                        Toast.makeText(StockDetailActivity.this, "No history data available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StockDetailActivity.this, "Failed to load history.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<StockHistory>>> call, Throwable t) {
                Toast.makeText(StockDetailActivity.this, "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
