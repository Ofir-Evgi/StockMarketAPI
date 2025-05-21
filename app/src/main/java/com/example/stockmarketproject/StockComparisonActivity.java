package com.example.stockmarketproject;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;
import com.example.stockmarketsdk.services.AnalyticsTracker;
import com.example.stockmarketsdk.views.StockComparisonView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StockComparisonActivity extends AppCompatActivity {

    private StockComparisonView comparisonView;
    private LineChart comparisonChart;
    private final List<LineDataSet> dataSets = new ArrayList<>();
    private long startTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_comparison);

        startTimeMillis = System.currentTimeMillis();

        comparisonView = findViewById(R.id.comparisonView);
        comparisonChart = findViewById(R.id.comparisonChart);
        Button backBtn = findViewById(R.id.btnBackToList);

        configureChart();

        ArrayList<Stock> stocks = (ArrayList<Stock>) getIntent().getSerializableExtra("stocks");

        if (stocks != null) {
            for (Stock stock : stocks) {
                comparisonView.addStock(stock);
                addStockToChart(stock);
            }
        }

        String userId = getPackageName();
        AnalyticsTracker.logScreenView(this, "stock_comparison", userId);

        backBtn.setOnClickListener(v -> {
            clearChart();
            comparisonView.clear();
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long duration = (System.currentTimeMillis() - startTimeMillis) / 1000;
        String userId = getPackageName();
        AnalyticsTracker.logScreenTime(this, "stock_comparison", duration, userId);
    }

    private void configureChart() {
        comparisonChart.setDrawGridBackground(false);
        comparisonChart.setNoDataText("Loading stock comparison...");
        comparisonChart.setTouchEnabled(true);
        comparisonChart.setDragEnabled(true);
        comparisonChart.setScaleEnabled(true);
        comparisonChart.setPinchZoom(true);
        comparisonChart.getDescription().setEnabled(false);
        comparisonChart.setExtraOffsets(12, 12, 12, 12);
        comparisonChart.getAxisRight().setEnabled(false);
        comparisonChart.getAxisLeft().setTextColor(Color.DKGRAY);
        comparisonChart.getAxisLeft().setTextSize(12f);
        comparisonChart.getAxisLeft().setDrawGridLines(true);
        comparisonChart.getXAxis().setEnabled(false);
        comparisonChart.getLegend().setTextSize(12f);
        comparisonChart.getLegend().setFormSize(10f);
    }

    private void addStockToChart(Stock stock) {
        List<StockPrice> history = stock.getPrices();
        if (history == null || history.size() < 2) return;

        List<Entry> entries = new ArrayList<>();
        float base = (float) history.get(0).getPrice();

        int interval = 10;
        for (int i = 0; i < history.size(); i += interval) {
            float current = (float) history.get(i).getPrice();
            float changePercent = ((current - base) / base) * 100;
            entries.add(new Entry(i, changePercent));
        }

        LineDataSet dataSet = new LineDataSet(entries, stock.getCompany_name() + " (" + stock.getSymbol() + ")");
        styleLineDataSet(dataSet, dataSets.size());
        dataSets.add(dataSet);
        updateCombinedChart();
    }

    private void styleLineDataSet(LineDataSet dataSet, int index) {
        int[] colors = {
                Color.parseColor("#F44336"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#FFC107")
        };
        int color = colors[index % colors.length];

        dataSet.setColor(color);
        dataSet.setLineWidth(2.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    private void updateCombinedChart() {
        LineData lineData = new LineData();
        for (LineDataSet set : dataSets) {
            lineData.addDataSet(set);
        }
        comparisonChart.setData(lineData);
        comparisonChart.invalidate();
    }

    private void clearChart() {
        dataSets.clear();
        comparisonChart.clear();
    }
}
