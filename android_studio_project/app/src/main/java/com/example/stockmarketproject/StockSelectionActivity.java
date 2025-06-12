package com.example.stockmarketproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.StockMarketSDK;

public class StockSelectionActivity extends AppCompatActivity {

    private static final String TAG = "StockSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: loading StockSelectionView...");

        FrameLayout container = new FrameLayout(this);
        container.setId(View.generateViewId());
        setContentView(container);

        StockMarketSDK.attachStockSelectionView(this, container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: detaching SDK...");
        StockMarketSDK.detach();
    }
}
