package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.stockmarketsdk.models.StockPrice;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MiniChartView extends FrameLayout {

    private LineChart chart;

    public MiniChartView(Context context) {
        super(context);
        init(context);
    }

    public MiniChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MiniChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        chart = new LineChart(context);
        addView(chart, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setExtraOffsets(4, 4, 4, 4);
    }

    public void setHistoryData(List<StockPrice> priceList) {
        if (priceList == null || priceList.isEmpty()) {
            chart.clear();
            return;
        }

        int maxPoints = 20;
        int step = Math.max(1, priceList.size() / maxPoints);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < priceList.size(); i += step) {
            entries.add(new Entry(entries.size(), (float) priceList.get(i).getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2f);
        dataSet.setColor(Color.parseColor("#1E88E5"));
        dataSet.setHighlightEnabled(false);

        chart.setData(new LineData(dataSet));
        chart.invalidate();
    }
}
