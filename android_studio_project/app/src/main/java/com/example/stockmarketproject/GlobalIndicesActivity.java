package com.example.stockmarketproject;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.StockMarketSDK;
import com.example.stockmarketsdk.services.AnalyticsTracker;

public class GlobalIndicesActivity extends AppCompatActivity {

    private long startTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_indices);

        startTimeMillis = System.currentTimeMillis();

        FrameLayout container = findViewById(R.id.globalIndexContainer);
        StockMarketSDK.attachGlobalIndexView(this, container);

        findViewById(R.id.btnBackToMain).setOnClickListener(v -> finish());

        String userId = getPackageName();
        AnalyticsTracker.logScreenView(this, "global_indices", userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        StockMarketSDK.detach();

        long durationSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        String userId = getPackageName();
        AnalyticsTracker.logScreenTime(this, "global_indices", durationSeconds, userId);
    }
}
