package com.example.stockmarketsdk.models;

import com.google.gson.annotations.SerializedName;

public class GlobalIndexPrice {
    private String time;
    private double price;

    @SerializedName("Percent")
    private String percent;

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public String getPercent() {
        return percent;
    }
}
