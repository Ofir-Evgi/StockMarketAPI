package com.example.stockmarketsdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FullMarketResponse {

    private List<Stock> stocks;

    @SerializedName("Global Indices")
    private List<GlobalIndexData> globalIndices;

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<GlobalIndexData> getGlobalIndices() {
        return globalIndices;
    }
}
