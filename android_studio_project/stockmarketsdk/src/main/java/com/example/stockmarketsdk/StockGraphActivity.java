package com.example.stockmarketsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.views.StockPageView;

public class StockGraphActivity extends Activity {

    private static final String EXTRA_STOCK = "extra_stock";

    public static void start(Context context, Stock stock) {
        Intent intent = new Intent(context, StockGraphActivity.class);
        intent.putExtra(EXTRA_STOCK, stock);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stock stock = (Stock) getIntent().getSerializableExtra(EXTRA_STOCK);

        StockPageView graphView = new StockPageView(this);
        graphView.setStock(stock);

        setContentView(graphView);
    }
}
