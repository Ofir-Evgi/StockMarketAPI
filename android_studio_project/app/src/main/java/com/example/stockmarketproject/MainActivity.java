package com.example.stockmarketproject;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stockmarketsdk.StockMarketSDK;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout container = new FrameLayout(this);
        container.setId(View.generateViewId());
        setContentView(container);

        StockMarketSDK.attachStockListView(
                this,
                container,
                Arrays.asList("AAPL","MSFT", "GOOGL","TSLA" , "META","NVDA"),
                5000
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StockMarketSDK.detach();
    }
}
