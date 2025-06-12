package com.example.stockmarketproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.views.StockPageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockGraphActivity extends AppCompatActivity {

    private static final String START_TIME = "07:00";
    private long startTimeMillis;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph);

        startTimeMillis = System.currentTimeMillis();

        stock = (Stock) getIntent().getSerializableExtra("stock");
        StockPageView graphView = findViewById(R.id.graphView);
        Button backBtn = findViewById(R.id.btnBack);
        TextView stockNameText = findViewById(R.id.stockNameText);

        if (stock != null) {
            String userId = getPackageName();

            AnalyticsTracker.logScreenView(this, "stock_graph", userId);
            AnalyticsTracker.logStockView(this, stock.getSymbol(), userId);

            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            List<StockPrice> filtered = new ArrayList<>();
            for (StockPrice price : stock.getPrices()) {
                String time = price.getTime();
                if (time.compareTo(START_TIME) >= 0 && time.compareTo(currentTime) <= 0) {
                    filtered.add(price);
                } else if (time.compareTo(currentTime) > 0) {
                    break;
                }
            }

            stockNameText.setText(stock.getCompany_name() + " (" + stock.getSymbol() + ")");

            if (filtered.isEmpty()) {
                Toast.makeText(this, "No graph data available for this time.", Toast.LENGTH_SHORT).show();
            } else {
                stock.setPrices(filtered);
                graphView.setStock(stock);
            }
        }

        backBtn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (stock != null) {
            long durationSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
            String userId = getPackageName();

            AnalyticsTracker.logScreenTime(this, "stock_graph", durationSeconds, userId);
        }
    }
}
