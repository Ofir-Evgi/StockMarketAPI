package com.example.stockmarketsdk.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StockPrice implements Serializable {

    @SerializedName("time")
    private String time;

    private double price;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
