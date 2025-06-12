package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.example.stockmarketsdk.models.StockPrice;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MiniChartView extends FrameLayout {

    private LineChart chart;

    private static final String COLOR_POSITIVE = "#10B981";
    private static final String COLOR_NEGATIVE = "#EF4444";
    private static final String COLOR_POSITIVE_FILL = "#4D10B981";
    private static final String COLOR_NEGATIVE_FILL = "#4DEF4444";

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

        setupChart();
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);

        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.setExtraOffsets(0f, 0f, 0f, 0f);

        chart.animateX(800);
    }

    public void setHistoryData(List<StockPrice> priceList) {
        if (priceList == null || priceList.isEmpty()) {
            chart.clear();
            return;
        }

        int maxPoints = 30;
        List<Entry> entries = optimizeDataPoints(priceList, maxPoints);

        if (entries.isEmpty()) {
            chart.clear();
            return;
        }

        boolean isPositiveTrend = isPositiveTrend(entries);

        LineDataSet dataSet = createDataSet(entries, isPositiveTrend);

        chart.setData(new LineData(dataSet));
        chart.invalidate();
    }

    private List<Entry> optimizeDataPoints(List<StockPrice> priceList, int maxPoints) {
        List<Entry> entries = new ArrayList<>();

        if (priceList.size() <= maxPoints) {
            for (int i = 0; i < priceList.size(); i++) {
                entries.add(new Entry(i, (float) priceList.get(i).getPrice()));
            }
        } else {
            double step = (double) priceList.size() / maxPoints;
            for (int i = 0; i < maxPoints; i++) {
                int index = (int) Math.round(i * step);
                if (index >= priceList.size()) index = priceList.size() - 1;
                entries.add(new Entry(i, (float) priceList.get(index).getPrice()));
            }
        }

        return entries;
    }

    private boolean isPositiveTrend(List<Entry> entries) {
        if (entries.size() < 2) return true;

        float firstPrice = entries.get(0).getY();
        float lastPrice = entries.get(entries.size() - 1).getY();

        return lastPrice >= firstPrice;
    }

    private LineDataSet createDataSet(List<Entry> entries, boolean isPositive) {
        LineDataSet dataSet = new LineDataSet(entries, "");

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.1f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);
        dataSet.setHighlightEnabled(false);

        dataSet.setLineWidth(2.5f);

        if (isPositive) {
            dataSet.setColor(Color.parseColor(COLOR_POSITIVE));
            dataSet.setFillColor(Color.parseColor(COLOR_POSITIVE));
        } else {
            dataSet.setColor(Color.parseColor(COLOR_NEGATIVE));
            dataSet.setFillColor(Color.parseColor(COLOR_NEGATIVE));
        }

        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(80);

        Drawable fillDrawable = createGradientDrawable(isPositive);
        dataSet.setFillDrawable(fillDrawable);

        return dataSet;
    }

    private Drawable createGradientDrawable(boolean isPositive) {
        // Create a gradient drawable programmatically
        android.graphics.drawable.GradientDrawable gradient =
                new android.graphics.drawable.GradientDrawable(
                        android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                isPositive ? Color.parseColor(COLOR_POSITIVE_FILL) : Color.parseColor(COLOR_NEGATIVE_FILL),
                                Color.TRANSPARENT
                        }
                );

        return gradient;
    }

    public void setTrendColor(boolean isPositive) {
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            LineDataSet dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);

            if (isPositive) {
                dataSet.setColor(Color.parseColor(COLOR_POSITIVE));
                dataSet.setFillColor(Color.parseColor(COLOR_POSITIVE));
            } else {
                dataSet.setColor(Color.parseColor(COLOR_NEGATIVE));
                dataSet.setFillColor(Color.parseColor(COLOR_NEGATIVE));
            }

            Drawable fillDrawable = createGradientDrawable(isPositive);
            dataSet.setFillDrawable(fillDrawable);

            chart.invalidate();
        }
    }

    public boolean isCurrentTrendPositive() {
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            LineDataSet dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            return dataSet.getColor() == Color.parseColor(COLOR_POSITIVE);
        }
        return true;
    }

    public void clearChart() {
        chart.clear();
        chart.invalidate();
    }

    public void setHistoryDataWithTrend(List<StockPrice> priceList, double changePercent) {
        if (priceList == null || priceList.isEmpty()) {
            chart.clear();
            return;
        }

        int maxPoints = 30;
        List<Entry> entries = optimizeDataPoints(priceList, maxPoints);

        if (entries.isEmpty()) {
            chart.clear();
            return;
        }

        boolean isPositiveTrend = changePercent >= 0;

        LineDataSet dataSet = createDataSet(entries, isPositiveTrend);
        chart.setData(new LineData(dataSet));
        chart.invalidate();
}
}