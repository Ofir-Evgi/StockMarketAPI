package com.example.stockmarketsdk.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.stockmarketsdk.StockMarketApi;
import com.example.stockmarketsdk.models.BaseResponse;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.example.stockmarketsdk.models.StockHistory;
import com.example.stockmarketsdk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockGraphView extends FrameLayout {

    private LineChart chart;
    private List<String> dates = new ArrayList<>();

    public StockGraphView(Context context) {
        super(context);
        init(context);
    }

    public StockGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StockGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        chart = new LineChart(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        addView(chart, params);

        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextSize(10f);
        leftAxis.setAxisLineWidth(1f);

        chart.getAxisRight().setEnabled(false);
    }

    public void setHistoryData(List<StockHistory> historyList) {
        if (historyList == null || historyList.isEmpty()) return;

        List<Entry> entries = new ArrayList<>();
        dates.clear();

        for (int i = 0; i < historyList.size(); i++) {
            StockHistory history = historyList.get(i);
            entries.add(new Entry(i, (float) history.getClose()));
            dates.add(formatDate(history.getDate()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Stock Prices");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(false);
        dataSet.setColor(0xFF009688);
        dataSet.setCircleColor(0xFF009688);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dates.size()) {
                    return dates.get(index);
                } else {
                    return "";
                }
            }
        });

        chart.invalidate();
    }


    public void loadHistoryBySymbol(String symbol, String range) {
        StockMarketApi.getStockHistory(symbol, range).enqueue(new Callback<BaseResponse<List<StockHistory>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<StockHistory>>> call, Response<BaseResponse<List<StockHistory>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setHistoryData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<StockHistory>>> call, Throwable t) {
            }
        });
    }


    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateString.split("T")[0]);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public void setStock(Stock stock) {
        if (stock == null || stock.getPrices() == null) return;

        List<Entry> entries = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<StockPrice> prices = stock.getPrices();

        String START_TIME = "07:00";
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        int intervalMinutes = 5;
        String lastAddedTime = "";

        for (StockPrice price : prices) {
            String time = price.getTime();
            if (time.compareTo(START_TIME) >= 0 && time.compareTo(currentTime) <= 0) {
                if (shouldKeepPoint(time, lastAddedTime, intervalMinutes)) {
                    entries.add(new Entry(entries.size(), (float) price.getPrice()));
                    times.add(time);
                    lastAddedTime = time;
                }
            } else if (time.compareTo(currentTime) > 0) {
                break;
            }
        }

        if (entries.isEmpty()) return;

        float firstPrice = entries.get(0).getY();
        float lastPrice = entries.get(entries.size() - 1).getY();
        boolean isRising = lastPrice >= firstPrice;

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet.setColor(isRising ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < times.size()) ? times.get(index) : "";
            }
        });

        chart.invalidate();
    }


    private boolean shouldKeepPoint(String current, String last, int minutes) {
        if (last.isEmpty()) return true;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date curr = sdf.parse(current);
            Date prev = sdf.parse(last);

            long diff = curr.getTime() - prev.getTime();
            return diff >= minutes * 60 * 1000;
        } catch (ParseException e) {
            return true;
        }
    }

    public void setStockPrices(List<StockPrice> prices) {
        if (prices == null || prices.isEmpty()) return;

        List<Entry> entries = new ArrayList<>();
        List<String> times = new ArrayList<>();

        String START_TIME = "07:00";
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        int intervalMinutes = 5;
        String lastAddedTime = "";

        double first = prices.get(0).getPrice();
        double last = prices.get(prices.size() - 1).getPrice();
        boolean isRising = last >= first;

        for (StockPrice price : prices) {
            String time = price.getTime();
            if (time.compareTo(START_TIME) >= 0 && time.compareTo(currentTime) <= 0) {
                if (shouldKeepPoint(time, lastAddedTime, intervalMinutes)) {
                    entries.add(new Entry(entries.size(), (float) price.getPrice()));
                    times.add(time);
                    lastAddedTime = time;
                }
            } else if (time.compareTo(currentTime) > 0) {
                break;
            }
        }

        if (entries.isEmpty()) return;

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setColor(isRising ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setHighlightEnabled(false);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < times.size()) ? times.get(index) : "";
            }
        });

        chart.invalidate();
    }
}
