package com.example.stockmarketproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.views.GlobalIndexView;

public class GlobalIndicesActivity extends AppCompatActivity {

    private GlobalIndexView globalIndexView;
    private long startTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_indices);

        startTimeMillis = System.currentTimeMillis();
        globalIndexView = findViewById(R.id.globalIndexView);
        globalIndexView.loadData();

        findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            finish();
        });

        String userId = getPackageName();
        AnalyticsTracker.logScreenView(this, "global_indices", userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long durationSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        String userId = getPackageName();
        AnalyticsTracker.logScreenTime(this, "global_indices", durationSeconds, userId);
    }
}
