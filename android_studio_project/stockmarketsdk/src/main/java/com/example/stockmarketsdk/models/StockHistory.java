package com.example.stockmarketsdk.models;

public class StockHistory {
    private String id;
    private String date;
    private double open;
    private double close;
    private double high;
    private double low;
    private int volume;

    public String getId() { return id; }
    public String getDate() { return date; }
    public double getOpen() { return open; }
    public double getClose() { return close; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public int getVolume() { return volume; }

    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setOpen(double open) { this.open = open; }
    public void setClose(double close) { this.close = close; }
    public void setHigh(double high) { this.high = high; }
    public void setLow(double low) { this.low = low; }
    public void setVolume(int volume) { this.volume = volume; }
}
